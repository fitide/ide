package org.main.ide.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ide.IdeController
import org.ide.LinkTreeController.Tree.ToolClasses.HintNode
import org.main.ide.uistate.UIColors

@Composable
fun EditorView(ide: IdeController) {

    val opened = ide.openedFileInfoState().value

    if (opened == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(UIColors.EditorBg),
            contentAlignment = Alignment.TopStart
        ) {
            Text(
                "No file opened",
                color = UIColors.TextPrimary,
                modifier = Modifier.padding(16.dp)
            )
        }
        return
    }

    val textValue = opened.textFieldValue.value

    val vScroll = rememberScrollState()
    val hScroll = rememberScrollState()

    var hints by remember { mutableStateOf<List<HintNode>>(emptyList()) }
    var hintsVisible by remember { mutableStateOf(false) }
    var selectedHint by remember { mutableStateOf(0) }

    var caretPx by remember { mutableStateOf(Offset.Zero) }
    var currentMapping by remember { mutableStateOf<OffsetMapping>(OffsetMapping.Identity) }

    var separatorXPx by remember { mutableStateOf<Float?>(null) }
    var firstLineSeparatorIndex by remember { mutableStateOf(-1) }

    var lastTextLayout by remember {
        mutableStateOf<TextLayoutResult?>(null)
    }

    val density = LocalDensity.current
    val focusRequester = remember { FocusRequester() }

    var folds by remember { mutableStateOf<List<FoldRegion>>(emptyList()) }

    LaunchedEffect(textValue.text) {
        folds = buildAutoFolds(
            ide.getSyntaxHighlightingForCurrentFile()
        )
    }

    val activeLineIndex =
        lineIndexForOffset(textValue.text, textValue.selection.start)

    val visualTransformation: VisualTransformation =
        remember(textValue.text, activeLineIndex, folds) {
            createEditorVisualTransformation(
                ide = ide,
                folds = folds
            ) { res ->
                currentMapping = res.offsetMapping
                firstLineSeparatorIndex = res.firstLineSeparatorIndex
            }
        }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UIColors.EditorBg)
            .verticalScroll(vScroll)
            .horizontalScroll(hScroll)
            .drawBehind {
                separatorXPx?.let { x ->
                    drawLine(
                        color = UIColors.DividerColor,
                        start = Offset(x, 0f),
                        end = Offset(x, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }
            .onPreviewKeyEvent { event ->

                if (event.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false

                val mod = event.isCtrlPressed || event.isMetaPressed

                when {
                    mod && event.key == Key.S -> {
                        ide.save()
                        return@onPreviewKeyEvent true
                    }
                    mod && event.key == Key.Z -> {
                        ide.undo()
                        return@onPreviewKeyEvent true
                    }
                    mod && event.key == Key.Y -> {
                        ide.redo()
                        return@onPreviewKeyEvent true
                    }
                }

                if (!hintsVisible || hints.isEmpty()) {
                    return@onPreviewKeyEvent false
                }

                when (event.key) {
                    Key.DirectionDown -> {
                        selectedHint = (selectedHint + 1) % hints.size
                        true
                    }
                    Key.DirectionUp -> {
                        selectedHint = (selectedHint - 1 + hints.size) % hints.size
                        true
                    }
                    Key.Enter, Key.Tab -> {
                        val (_, range) = extractPrefixAndRange(textValue)
                        val h = hints[selectedHint]
                        val t = applyHint(textValue.text, range, h)

                        ide.onTextChanged(
                            textValue.copy(
                                text = t,
                                selection = TextRange(range.start + h.name.length)
                            )
                        )

                        hintsVisible = false
                        focusRequester.requestFocus()
                        true
                    }
                    Key.Escape -> {
                        hintsVisible = false
                        focusRequester.requestFocus()
                        true
                    }
                    else -> false
                }
            }
    ) {
        fun tryToggleFold(click: Offset): Boolean {
            val layout = lastTextLayout ?: return false
            val transformedOffset = layout.getOffsetForPosition(click)
            val originalOffset =
                currentMapping.transformedToOriginal(transformedOffset)
            val line =
                lineIndexForOffset(textValue.text, originalOffset)
            val fold = folds.firstOrNull { it.startLine == line }
                ?: return false

            folds = folds.map {
                if (it == fold)
                    it.copy(collapsed = !it.collapsed)
                else
                    it
            }

            return true
        }

        BasicTextField(
            value = textValue,
            onValueChange = { newValue ->
                ide.onTextChanged(newValue)

                val (prefix, _) = extractPrefixAndRange(newValue)
                if (prefix.isNotEmpty()) {

                    val list = ide.getHintsForCurrentFile(prefix)
                        .filter { it.name != prefix }
                        .toList()

                    hints = list
                    hintsVisible = list.isNotEmpty()
                    selectedHint = 0

                } else {
                    hintsVisible = false
                    hints = emptyList()
                }
            },

            textStyle = TextStyle(
                color = UIColors.TextPrimary,
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace
            ),
            cursorBrush = SolidColor(UIColors.TextPrimary),
            visualTransformation = visualTransformation,
            onTextLayout = { layout ->
                lastTextLayout = layout
                val origCaret = textValue.selection.end
                val mapped = currentMapping.originalToTransformed(origCaret)
                val caretRect = layout.getCursorRect(mapped)
                caretPx = caretRect.bottomLeft

                val sepIndex = firstLineSeparatorIndex
                if (sepIndex >= 0 && sepIndex < layout.layoutInput.text.length) {
                    val box = layout.getBoundingBox(sepIndex)
                    separatorXPx = box.centerLeft.x
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp)
                .focusRequester(focusRequester)
                .pointerInput(Unit) {
                    detectTapGestures { pos ->
                        if (pos.x < 48.dp.toPx()) {
                            if (tryToggleFold(pos)) {
                            }
                        }
                    }
                }

        )

        if (hintsVisible && hints.isNotEmpty()) {

            val offset = with(density) {
                DpOffset(
                    caretPx.x.toDp(),
                    caretPx.y.toDp() + 4.dp
                )
            }

            HintPopup(
                hints = hints,
                selected = selectedHint,
                offset = offset,
                onSelect = { selectedHint = it },
                onClick = { hint ->
                    val (_, range) = extractPrefixAndRange(textValue)
                    val t = applyHint(textValue.text, range, hint)

                    ide.onTextChanged(
                        textValue.copy(
                            text = t,
                            selection = TextRange(range.start + hint.name.length)
                        )
                    )

                    hintsVisible = false
                    focusRequester.requestFocus()
                }
            )
        }
    }
}
