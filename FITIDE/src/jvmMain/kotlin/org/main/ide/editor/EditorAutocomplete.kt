package org.main.ide.editor

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import org.ide.LinkTreeController.Tree.ToolClasses.HintNode

private fun isWordChar(ch: Char) = ch.isLetterOrDigit() || ch == '_'

internal fun extractPrefixAndRange(v: TextFieldValue): Pair<String, TextRange> {
    val text = v.text
    val caret = v.selection.start.coerceIn(0, text.length)

    if (text.isEmpty()) return "" to TextRange(caret, caret)

    var start = caret
    while (start > 0 && isWordChar(text[start - 1])) start--

    var end = caret
    while (end < text.length && isWordChar(text[end])) end++

    return text.substring(start, caret) to TextRange(start, end)
}

internal fun applyHint(text: String, range: TextRange, hint: HintNode): String =
    text.substring(0, range.start) + hint.name + text.substring(range.end)
