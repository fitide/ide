package org.main.ide.editor.folding

import org.antlr.v4.runtime.tree.ParseTree
import org.ide.PluginController.PluginInterface.Plugin

fun buildFoldsFromParseTree(
    root: ParseTree,
    plugin: Plugin
): List<FoldRegion> {

    val result = mutableListOf<FoldRegion>()

    fun visit(node: ParseTree) {
        val bounds = plugin.getBounds(node)
        val keywords = plugin.getKeyWordsOfModule(node)

        val hasManualControlKeyword = if (keywords.isNullOrEmpty()) {
            (0 until node.childCount).any { i ->
                val childText = node.getChild(i).text
                when (childText.lowercase()) {
                    "if", "while", "until", "for", "repeat" -> true
                    else -> false
                }
            }
        } else false

        val isControlBlock = hasManualControlKeyword || (keywords?.any {
            when (it.text.lowercase()) {
                "if", "while", "until", "for", "repeat" -> true
                else -> false
            }
        } == true)

        val body = plugin.getPositionOfModuleBody(node)

        if (bounds != null) {
            if (isControlBlock) {
                if (bounds.rowE > bounds.rowS) {
                    result += FoldRegion(
                        startLine = bounds.rowS,
                        endLine = bounds.rowE,
                        collapsed = false
                    )
                }
            }
            else if (!keywords.isNullOrEmpty() && body != null) {
                if (body.rowE > bounds.rowS) {
                    result += FoldRegion(
                        startLine = bounds.rowS,
                        endLine = body.rowE,
                        collapsed = false
                    )
                }
            }
        }

        repeat(node.childCount) { visit(node.getChild(it)) }
    }

    visit(root)

    val normalized = result
        .sortedWith(compareBy({ it.startLine }, { -it.endLine }))
        .fold(mutableListOf<FoldRegion>()) { acc, cur ->
            if (acc.none { it.startLine == cur.startLine && it.endLine == cur.endLine }) {
                acc += cur
            }
            acc
        }

    return normalized.sortedBy { it.startLine }
}