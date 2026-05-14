package org.main.ide.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.ide.GoToResult
import org.ide.IdeController
import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour
import org.ide.LinkTreeController.Tree.ToolClasses.HintNode
import org.main.ide.editor.folding.FoldingTransformation
import org.main.ide.editor.folding.FoldRegion
import org.main.ide.editor.folding.buildFoldsFromParseTree
import org.main.ide.uistate.UIColors

@Composable
fun EditorView(ide: IdeController) {

    val opened = ide.openedFileInfoState().value ?: run {
        Box(modifier = Modifier.fillMaxSize().background(UIColors.EditorBg)) {
            Text("No file opened", color = UIColors.TextPrimary, modifier = Modifier.padding(16.dp))
        }
        return
    }

    val textValue = opened.textFieldValue.value
    val vScroll = rememberScrollState()
    val hScroll = rememberScrollState()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    val fontSize = 14.sp
    val lineHeight = 18.sp
    val editorPadding = 6.dp

    var folds by remember { mutableStateOf<List<FoldRegion>>(emptyList()) }
    var syntaxHighlight by remember { mutableStateOf<List<CodeStrForColour>>(emptyList()) }
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    var caretPx by remember { mutableStateOf(Offset.Zero) }

    val density = LocalDensity.current
    val focusRequester = remember { FocusRequester() }

    var pendingGoTo by remember { mutableStateOf<GoToResult?>(null) }
    var scrollToCaretAfterGoTo by remember { mutableStateOf(false) }

    LaunchedEffect(pendingGoTo) {
        val req = pendingGoTo ?: return@LaunchedEffect
        try {
            ide.openFile(req.file)

            val openedNow = ide.openedFileInfoState().value ?: return@LaunchedEffect
            val curText = openedNow.textFieldValue.value.text

            val off = rowColToOffset0(curText, req.row, req.col)
            ide.onTextChanged(openedNow.textFieldValue.value.copy(selection = TextRange(off)))

            focusRequester.requestFocus()
            scrollToCaretAfterGoTo = true
        } catch (_: Exception) {
        } finally {
            pendingGoTo = null
        }
    }

    LaunchedEffect(textValue.text) {
        if (textValue.text.isEmpty()) return@LaunchedEffect
        delay(2000)
        syntaxHighlight = ide.getSyntaxHighlightingForCurrentFile()
        val tree = ide.getCurrentParseTree()
        val plugin = ide.getCurrentPlugin()
        if (tree != null && plugin != null) {
            val newFolds = buildFoldsFromParseTree(tree, plugin)
            val prevMap = folds.associateBy({ it.startLine to it.endLine }, { it.collapsed })
            folds = newFolds.map { it.copy(collapsed = prevMap[it.startLine to it.endLine] ?: false) }
        }
    }

    LaunchedEffect(opened) {
        syntaxHighlight = ide.getSyntaxHighlightingForCurrentFile()
        val tree = ide.getCurrentParseTree()
        val plugin = ide.getCurrentPlugin()
        if (tree != null && plugin != null) {
            folds = buildFoldsFromParseTree(tree, plugin)
        }
    }

    val folding = remember(folds) { FoldingTransformation(folds) }

    val visualTransformation = remember(syntaxHighlight, folding) {
        val syntaxTransformer = createSyntaxHighlightTransformation(syntaxHighlight)
        VisualTransformation { original ->
            val highlighted = syntaxTransformer.filter(original)
            val folded = folding.filter(highlighted.text)
            TransformedText(folded.text, object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int =
                    folded.offsetMapping.originalToTransformed(highlighted.offsetMapping.originalToTransformed(offset))
                override fun transformedToOriginal(offset: Int): Int =
                    highlighted.offsetMapping.transformedToOriginal(folded.offsetMapping.transformedToOriginal(offset))
            })
        }
    }

    var hints by remember { mutableStateOf<List<HintNode>>(emptyList()) }
    var hintsVisible by remember { mutableStateOf(false) }
    var selectedHint by remember { mutableStateOf(0) }

    LaunchedEffect(textValue.selection, textLayoutResult) {
        val layout = textLayoutResult ?: return@LaunchedEffect
        val transformed = visualTransformation.filter(AnnotatedString(textValue.text))
        val mapped = transformed.offsetMapping.originalToTransformed(textValue.selection.end)
        val offsetSafe = mapped.coerceIn(0, layout.layoutInput.text.length)

        val rect = layout.getCursorRect(offsetSafe)
        caretPx = Offset(x = rect.left, y = rect.top)

        val extraPadding = with(density) { 24.dp.toPx() }
        bringIntoViewRequester.bringIntoView(
            rect.copy(
                top = rect.top - extraPadding,
                bottom = rect.bottom + extraPadding
            )
        )
    }

    LaunchedEffect(scrollToCaretAfterGoTo, textLayoutResult, textValue.selection) {
        if (!scrollToCaretAfterGoTo) return@LaunchedEffect
        val layout = textLayoutResult ?: return@LaunchedEffect

        val transformed = visualTransformation.filter(AnnotatedString(textValue.text))
        val mapped = transformed.offsetMapping.originalToTransformed(textValue.selection.end)
        val offsetSafe = mapped.coerceIn(0, layout.layoutInput.text.length)

        val rect = layout.getCursorRect(offsetSafe)
        val extraPadding = with(density) { 24.dp.toPx() }
        bringIntoViewRequester.bringIntoView(
            rect.copy(
                top = rect.top - extraPadding,
                bottom = rect.bottom + extraPadding
            )
        )

        scrollToCaretAfterGoTo = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UIColors.EditorBg)
            .onPreviewKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false
                val mod = event.isCtrlPressed || event.isMetaPressed

                if (event.key == Key.F12 || (mod && event.key == Key.B)) {
                    val (row, col) = offsetToRowCol0(textValue.text, textValue.selection.end)
                    val r = ide.goToDefinition(row, col)
                    if (r != null) {
                        pendingGoTo = r
                        return@onPreviewKeyEvent true
                    }
                    return@onPreviewKeyEvent false
                }

                when {
                    mod && event.key == Key.S -> { ide.save(); true }
                    mod && event.key == Key.Z -> { ide.undo(); true }
                    mod && event.key == Key.Y -> { ide.redo(); true }
                    hintsVisible && hints.isNotEmpty() -> when (event.key) {
                        Key.DirectionDown -> { selectedHint = (selectedHint + 1) % hints.size; true }
                        Key.DirectionUp -> { selectedHint = (selectedHint - 1 + hints.size) % hints.size; true }
                        Key.Enter, Key.Tab -> {
                            val (_, range) = extractPrefixAndRange(textValue)
                            val hint = hints[selectedHint]
                            ide.onTextChanged(textValue.copy(
                                text = applyHint(textValue.text, range, hint),
                                selection = TextRange(range.start + hint.name.length)
                            ))
                            hintsVisible = false
                            true
                        }
                        Key.Escape -> { hintsVisible = false; true }
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
                    val list = ide.getHintsForCurrentFile(prefix).filter { it.name != prefix }
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
                .verticalScroll(vScroll)
                .bringIntoViewRequester(bringIntoViewRequester),
            textStyle = TextStyle(
                color = UIColors.TextPrimary,
                fontSize = fontSize,
                lineHeight = lineHeight,
                fontFamily = FontFamily.Monospace
            ),
            cursorBrush = SolidColor(UIColors.TextPrimary),
            visualTransformation = visualTransformation,
            onTextLayout = { textLayoutResult = it },
            decorationBox = { innerTextField ->
                Row {
                    LineNumberGutter(
                        visibleToOriginal = folding.lastVisibleToOriginal,
                        folds = folds,
                        onToggleFold = { originalLine ->
                            folds = folds.map {
                                if (it.startLine == originalLine) it.copy(collapsed = !it.collapsed) else it
                            }
                        },
                        lineHeight = lineHeight,
                        topPadding = editorPadding,
                        ide = ide
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .horizontalScroll(hScroll)
                            .padding(top = editorPadding, start = 8.dp, bottom = editorPadding)
                    ) {
                        innerTextField()
                    }
                }
            }
        )

        if (hintsVisible && hints.isNotEmpty()) {
            val xDp = with(density) { (caretPx.x).toDp() + 8.dp }
            val yDp = with(density) { (caretPx.y).toDp() + lineHeight.toDp() + editorPadding }

            HintPopup(
                hints = hints,
                selected = selectedHint,
                offset = DpOffset(xDp, yDp),
                onSelect = { selectedHint = it },
                onClick = { hint ->
                    val (_, range) = extractPrefixAndRange(textValue)
                    ide.onTextChanged(textValue.copy(
                        text = applyHint(textValue.text, range, hint),
                        selection = TextRange(range.start + hint.name.length)
                    ))
                    hintsVisible = false
                }
            )
        }
    }
}

private fun offsetToRowCol0(text: String, offset: Int): Pair<Int, Int> {
    val safe = offset.coerceIn(0, text.length)
    var row = 0
    var lineStart = 0
    for (i in 0 until safe) {
        if (text[i] == '\n') {
            row++
            lineStart = i + 1
        }
    }
    val col = safe - lineStart
    return row to col
}

private fun rowColToOffset0(text: String, row: Int, col: Int): Int {
    val r = row.coerceAtLeast(0)
    val c = col.coerceAtLeast(0)

    var curRow = 0
    var i = 0
    while (curRow < r && i < text.length) {
        if (text[i] == '\n') curRow++
        i++
    }
    val lineStart = i
    val lineEnd = text.indexOf('\n', startIndex = lineStart).let { if (it == -1) text.length else it }
    return (lineStart + c).coerceIn(lineStart, lineEnd)
}
