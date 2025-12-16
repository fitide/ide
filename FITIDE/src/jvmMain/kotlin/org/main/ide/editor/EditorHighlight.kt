package org.main.ide.editor

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import org.ide.IdeController
import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour
import org.main.ide.uistate.UIColors

internal data class HighlightResult(
    val annotated: AnnotatedString,
    val offsetMapping: OffsetMapping,
    val firstLineSeparatorIndex: Int
)

internal fun buildHighlightedWithLineNumbers(
    raw: String,
    tokens: List<CodeStrForColour>,
): HighlightResult {

    val lines = raw.split('\n')
    val count = lines.size
    val maxDigits = maxOf(count.toString().length, 2)

    val builder = AnnotatedString.Builder()

    val origLineStart = IntArray(count)
    val lineLen       = IntArray(count)
    val transfLineStart = IntArray(count)
    val prefixLen       = IntArray(count)

    var origIndex = 0
    for (i in 0 until count) {
        origLineStart[i] = origIndex
        val len = lines[i].length
        lineLen[i] = len
        origIndex += len
        if (i < count - 1) origIndex++
    }

    fun tokensForLine(i: Int) =
        tokens.filter { it.pos?.rowS == i }

    val activeBg = UIColors.EditorBg.copy(alpha = 0.25f)

    var tIndex = 0
    var firstLineSeparatorIndex = -1

    for (i in 0 until count) {
        val line = lines[i]
        val num = (i + 1).toString().padStart(maxDigits, ' ')
        val prefix = " $num  "
        val pStart = tIndex
        builder.append(prefix)
        val pEnd = pStart + prefix.length

        transfLineStart[i] = pStart
        prefixLen[i] = prefix.length

        builder.addStyle(
            SpanStyle(
                color = UIColors.TextSecondary,
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace
            ),
            pStart,
            pEnd
        )

        if (i == 0) {
            firstLineSeparatorIndex = pStart + prefix.length - 1
        }

        val textStart = pEnd

        builder.append(line)

        for (tok in tokensForLine(i)) {
            val pos = tok.pos ?: continue
            val tag = tok.tag ?: continue
            val sc = pos.colS.coerceIn(0, line.length)
            val ec = pos.colE.coerceIn(0, line.length)
            if (sc < ec) {
                builder.addStyle(
                    SpanStyle(
                        color = colorForTag(tag),
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Monospace
                    ),
                    textStart + sc,
                    textStart + ec
                )
            }
        }

        tIndex = textStart + line.length
        if (i < count - 1) {
            builder.append('\n')
            tIndex++
        }
    }

    val mapping = object : OffsetMapping {

        override fun originalToTransformed(offset: Int): Int {
            if (count == 0) return 0
            val safe = offset.coerceIn(0, raw.length)

            var line = 0
            while (line + 1 < count && origLineStart[line + 1] <= safe) line++

            val col = safe - origLineStart[line]
            return transfLineStart[line] + prefixLen[line] + col.coerceIn(0, lineLen[line])
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (count == 0) return 0
            val safe = offset.coerceIn(0, builder.length)

            var line = 0
            while (line + 1 < count && transfLineStart[line + 1] <= safe) line++

            val pStart = transfLineStart[line]
            val pLen   = prefixLen[line]
            val cStart = pStart + pLen
            val cEnd   = cStart + lineLen[line]
            val oStart = origLineStart[line]

            return when {
                safe <= cStart -> oStart
                safe <= cEnd   -> oStart + (safe - cStart)
                else           -> oStart + lineLen[line]
            }
        }
    }

    return HighlightResult(builder.toAnnotatedString(), mapping, firstLineSeparatorIndex)
}

internal fun createEditorVisualTransformation(
    ide: IdeController,
    text: String,
    onResult: (HighlightResult) -> Unit
): VisualTransformation {
    return object : VisualTransformation {
        override fun filter(original: AnnotatedString): TransformedText {
            val res = buildHighlightedWithLineNumbers(
                raw = original.text,
                tokens = ide.getSyntaxHighlightingForCurrentFile()
            )
            onResult(res)
            return TransformedText(res.annotated, res.offsetMapping)
        }
    }
}
