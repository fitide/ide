package org.main.ide.editor.folding

import androidx.compose.ui.text.input.OffsetMapping

class FoldingOffsetMapping(
    private val originalText: String,
    private val transformedText: String,
    private val visibleToOriginal: IntArray
) : OffsetMapping {

    private val originalLineOffsets = computeLineOffsets(originalText)
    private val transformedLineOffsets = computeLineOffsets(transformedText)

    override fun originalToTransformed(offset: Int): Int {
        val (origLine, col) = findLineAndColumn(originalLineOffsets, offset)

        val visibleLine =
            visibleToOriginal.indexOfFirst { it == origLine }
                .coerceAtLeast(0)

        val visibleLineOffset = transformedLineOffsets[visibleLine]
        return visibleLineOffset + col.coerceAtMost(transformedLineLength(visibleLine))
    }

    override fun transformedToOriginal(offset: Int): Int {
        val (visibleLine, col) = findLineAndColumn(transformedLineOffsets, offset)
        val originalLine =
            visibleToOriginal.getOrElse(visibleLine) { visibleToOriginal.last() }

        val originalLineOffset = originalLineOffsets[originalLine]
        return originalLineOffset + col.coerceAtMost(originalLineLength(originalLine))
    }


    private fun computeLineOffsets(text: String): IntArray {
        val result = mutableListOf(0)
        text.forEachIndexed { index, c ->
            if (c == '\n') result += index + 1
        }
        return result.toIntArray()
    }

    private fun findLineAndColumn(offsets: IntArray, offset: Int): Pair<Int, Int> {
        val line = offsets.indexOfLast { it <= offset }.coerceAtLeast(0)
        return line to (offset - offsets[line])
    }

    private fun originalLineLength(line: Int): Int {
        val start = originalLineOffsets[line]
        val end =
            if (line + 1 < originalLineOffsets.size)
                originalLineOffsets[line + 1] - 1
            else
                originalText.length
        return end - start
    }

    private fun transformedLineLength(line: Int): Int {
        val start = transformedLineOffsets[line]
        val end =
            if (line + 1 < transformedLineOffsets.size)
                transformedLineOffsets[line + 1] - 1
            else
                transformedText.length
        return end - start
    }
}
