package org.main.ide.editor

internal fun lineIndexForOffset(text: String, offset: Int): Int {
    val safe = offset.coerceIn(0, text.length)
    var line = 0
    for (i in 0 until safe) {
        if (text[i] == '\n') line++
    }
    return line
}
