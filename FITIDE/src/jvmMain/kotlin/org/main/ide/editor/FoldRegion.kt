package org.main.ide.editor

import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour

data class FoldRegion(
    val startLine: Int,
    val endLine: Int,
    val collapsed: Boolean = false
)

internal fun buildAutoFolds(
    tokens: List<CodeStrForColour>
): List<FoldRegion> {

    return tokens
        .mapNotNull { tok ->
            val pos = tok.pos ?: return@mapNotNull null
            if (pos.rowE > pos.rowS) {
                FoldRegion(
                    startLine = pos.rowS,
                    endLine = pos.rowE,
                    collapsed = false
                )
            } else null
        }
        .filter { it.endLine - it.startLine >= 1 }
        .distinctBy { it.startLine to it.endLine }
        .sortedBy { it.startLine }
}
