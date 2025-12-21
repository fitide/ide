package org.main.ide.editor

internal fun offsetFor(
    row: Int,
    col: Int,
    text: String
): Int {

    if (row < 0 || col < 0) return 0

    var currentRow = 0
    var offset = 0

    while (offset < text.length && currentRow < row) {
        if (text[offset] == '\n') {
            currentRow++
        }
        offset++
    }

    return (offset + col).coerceIn(0, text.length)
}
