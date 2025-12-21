package org.main.ide.editor.folding

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class FoldingTransformation(
    private val folds: List<FoldRegion>
) : VisualTransformation {

    var lastVisibleToOriginal: IntArray = intArrayOf()
        private set

    override fun filter(text: AnnotatedString): TransformedText {
        val originalLines = text.text.split('\n')

        val visibleLines = mutableListOf<String>()
        val visibleToOriginal = mutableListOf<Int>()

        var line = 0
        while (line < originalLines.size) {
            val fold = folds.firstOrNull { it.collapsed && it.startLine == line }

            if (fold != null) {
                val header = originalLines[line]
                val footer = originalLines[fold.endLine]
                visibleLines += "$header … $footer"
                visibleToOriginal += line
                line = fold.endLine + 1
            } else {
                visibleLines += originalLines[line]
                visibleToOriginal += line
                line++
            }
        }

        lastVisibleToOriginal = visibleToOriginal.toIntArray()
        val transformedText = visibleLines.joinToString("\n")
        val max = transformedText.length
        val safeSpanStyles = text.spanStyles.mapNotNull { range ->
            val s = range.start.coerceIn(0, max)
            val e = range.end.coerceIn(0, max)
            if (s >= e) null else AnnotatedString.Range(range.item, s, e)
        }
        val safeParagraphStyles = text.paragraphStyles.mapNotNull { range ->
            val s = range.start.coerceIn(0, max)
            val e = range.end.coerceIn(0, max)
            if (s >= e) null else AnnotatedString.Range(range.item, s, e)
        }

        val transformedAnnotated = AnnotatedString(
            text = transformedText,
            spanStyles = safeSpanStyles,
            paragraphStyles = safeParagraphStyles
        )

        return TransformedText(
            transformedAnnotated,
            FoldingOffsetMapping(
                originalText = text.text,
                transformedText = transformedText,
                visibleToOriginal = lastVisibleToOriginal
            )
        )
    }
}
