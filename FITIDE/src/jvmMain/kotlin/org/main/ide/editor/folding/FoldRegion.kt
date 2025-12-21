package org.main.ide.editor.folding

data class FoldRegion(
    val startLine: Int,
    val endLine: Int,
    val collapsed: Boolean = false
)
