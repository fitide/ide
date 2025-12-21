package org.main.ide.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ide.IdeController
import org.ide.LinkTreeController.Tree.ToolClasses.HintNode
import org.main.ide.editor.folding.FoldingTransformation
import org.main.ide.editor.folding.FoldRegion
import org.main.ide.editor.folding.buildFoldsFromParseTree
import org.main.ide.uistate.UIColors

@Composable
fun EditorView(ide: IdeController) {

    val opened = ide.openedFileInfoState().value ?: run {
        Box(
            modifier = Modifier.fillMaxSize().background(UIColors.EditorBg),
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

    val fontSize = 14.sp
    val lineHeight = 18.sp
    val editorPadding = 6.dp

    var folds by remember { mutableStateOf<List<FoldRegion>>(emptyList()) }

    LaunchedEffect(opened) {
        val tree = ide.getCurrentParseTree()
        val plugin = ide.getCurrentPlugin()

        if (tree != null && plugin != null) {
            val newFolds = buildFoldsFromParseTree(tree, plugin)
            val prev =
                folds.associateBy({ it.startLine to it.endLine }, { it.collapsed })

            folds = newFolds
                .sortedBy { it.startLine }
                .map {
                    it.copy(collapsed = prev[it.startLine to it.endLine] ?: false)
                }
        }
    }

    val folding = remember(folds, opened) {
        FoldingTransformation(folds)
    }

    val visualTransformation = remember(folding) {
        createSyntaxHighlightTransformation(ide)
            .then(folding)
    }

    var hints by remember { mutableStateOf<List<HintNode>>(emptyList()) }
    var hintsVisible by remember { mutableStateOf(false) }
    var selectedHint by remember { mutableStateOf(0) }
    var caretPx by remember { mutableStateOf(Offset.Zero) }

    val density = LocalDensity.current
    val focusRequester = remember { FocusRequester() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UIColors.EditorBg)
            .onPreviewKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false

                val mod = event.isCtrlPressed || event.isMetaPressed

                when {
                    mod && event.key == Key.S -> {
                        try { ide.save() } catch (_: Exception) {}
                        true
                    }

                    mod && event.key == Key.Z -> {
                        ide.undo()
                        true
                    }

                    mod && event.key == Key.Y -> {
                        ide.redo()
                        true
                    }

                    hintsVisible && hints.isNotEmpty() -> when (event.key) {
                        Key.DirectionDown -> {
                            selectedHint = (selectedHint + 1) % hints.size
                            true
                        }

                        Key.DirectionUp -> {
                            selectedHint = (selectedHint - 1 + hints.size) % hints.size
                            true
                        }

                        Key.Enter, Key.Tab -> {
                            val (prefix, range) = extractPrefixAndRange(textValue)
                            val hint = hints[selectedHint]

                            ide.onTextChanged(
                                textValue.copy(
                                    text = applyHint(textValue.text, range, hint),
                                    selection = TextRange(range.start + hint.name.length)
                                )
                            )
                            hintsVisible = false
                            true
                        }

                        Key.Escape -> {
                            hintsVisible = false
                            true
                        }

                        else -> false
                    }

                    else -> false
                }
            }
    ) {

        BasicTextField(
            value = textValue,
            onValueChange = { newValue ->
                ide.onTextChanged(newValue)

                val (prefix, _) = extractPrefixAndRange(newValue)
                if (prefix.isNotEmpty()) {
                    val list =
                        ide.getHintsForCurrentFile(prefix)
                            .filter { it.name != prefix }

                    hints = list
                    hintsVisible = list.isNotEmpty()
                    selectedHint = 0
                } else {
                    hintsVisible = false
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)
                .verticalScroll(vScroll),
            textStyle = TextStyle(
                color = UIColors.TextPrimary,
                fontSize = fontSize,
                lineHeight = lineHeight,
                fontFamily = FontFamily.Monospace
            ),
            cursorBrush = SolidColor(UIColors.TextPrimary),
            visualTransformation = visualTransformation,
            onTextLayout = { layout ->
                val mapped = visualTransformation
                    .filter(AnnotatedString(textValue.text))
                    .offsetMapping
                    .originalToTransformed(textValue.selection.end)

                caretPx = layout.getCursorRect(mapped).bottomLeft
            },
            decorationBox = { innerTextField ->
                Row {
                    LineNumberGutter(
                        visibleToOriginal = folding.lastVisibleToOriginal,
                        folds = folds,
                        onToggleFold = { originalLine ->
                            folds = folds.map {
                                if (it.startLine == originalLine)
                                    it.copy(collapsed = !it.collapsed)
                                else it
                            }
                        },
                        lineHeight = lineHeight,
                        topPadding = editorPadding
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .horizontalScroll(hScroll)
                            .padding(
                                top = editorPadding,
                                start = 8.dp,
                                bottom = editorPadding
                            )
                    ) {
                        innerTextField()
                    }
                }
            }
        )

        if (hintsVisible && hints.isNotEmpty()) {
            val offset = with(density) {
                DpOffset(
                    x = caretPx.x.toDp(),
                    y = caretPx.y.toDp()
                )
            }

            HintPopup(
                hints = hints,
                selected = selectedHint,
                offset = offset,
                onSelect = { selectedHint = it },
                onClick = { hint ->
                    val (_, range) = extractPrefixAndRange(textValue)
                    ide.onTextChanged(
                        textValue.copy(
                            text = applyHint(textValue.text, range, hint),
                            selection = TextRange(range.start + hint.name.length)
                        )
                    )
                    hintsVisible = false
                }
            )
        }

    }
}

private fun VisualTransformation.then(
    second: VisualTransformation
): VisualTransformation {
    val first = this
    return VisualTransformation { original ->
        val t1 = first.filter(original)
        val t2 = second.filter(t1.text)

        TransformedText(
            t2.text,
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    val mid = t1.offsetMapping.originalToTransformed(offset)
                    return t2.offsetMapping.originalToTransformed(mid)
                }

                override fun transformedToOriginal(offset: Int): Int {
                    val mid = t2.offsetMapping.transformedToOriginal(offset)
                    return t1.offsetMapping.transformedToOriginal(mid)
                }
            }
        )
    }
}
