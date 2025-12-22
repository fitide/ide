package org.main.ide.editor.folding

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class FoldingTransformation(
    private val folds: List<FoldRegion>
) : VisualTransformation {

    var lastVisibleToOriginal: IntArray = intArrayOf()
        private set

    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val lineStarts = computeLineStarts(originalText)
        val lineCount = lineStarts.size

        val visibleLineIndices = mutableListOf<Int>()
        for (i in 0 until lineCount) {
            if (!isHidden(i)) visibleLineIndices.add(i)
        }
        lastVisibleToOriginal = visibleLineIndices.toIntArray()

        val transformedBuilder = AnnotatedString.Builder()
        val mapping = FoldingOffsetMapping()

        for (lineIdx in visibleLineIndices) {
            val start = lineStarts[lineIdx]
            val end = if (lineIdx + 1 < lineCount) lineStarts[lineIdx + 1] else originalText.length

            val currentTransformedStart = transformedBuilder.length

            // ВАЖНО: subSequence сохраняет цвета из предыдущего шага (подсветки)
            transformedBuilder.append(text.subSequence(start, end))

            mapping.addRange(
                originalStart = start,
                transformedStart = currentTransformedStart,
                length = end - start
            )
        }

        return TransformedText(transformedBuilder.toAnnotatedString(), mapping)
    }

    private fun isHidden(line: Int): Boolean =
        folds.any { it.collapsed && line in (it.startLine + 1 until it.endLine) }

    private fun computeLineStarts(text: String): IntArray {
        val starts = mutableListOf(0)
        text.forEachIndexed { i, c -> if (c == '\n') starts.add(i + 1) }
        return starts.toIntArray()
    }
}

class FoldingOffsetMapping : OffsetMapping {
    private val ranges = mutableListOf<MappingRange>()
    private data class MappingRange(val origStart: Int, val transStart: Int, val length: Int)

    fun addRange(originalStart: Int, transformedStart: Int, length: Int) {
        ranges.add(MappingRange(originalStart, transformedStart, length))
    }

    override fun originalToTransformed(offset: Int): Int {
        for (range in ranges) {
            if (offset >= range.origStart && offset < range.origStart + range.length) {
                return range.transStart + (offset - range.origStart)
            }
        }
        return ranges.firstOrNull { it.origStart > offset }?.transStart
            ?: ranges.lastOrNull()?.let { it.transStart + it.length } ?: 0
    }

    override fun transformedToOriginal(offset: Int): Int {
        for (range in ranges) {
            if (offset >= range.transStart && offset < range.transStart + range.length) {
                return range.origStart + (offset - range.transStart)
            }
        }
        return ranges.lastOrNull()?.let { it.origStart + it.length } ?: 0
    }
}