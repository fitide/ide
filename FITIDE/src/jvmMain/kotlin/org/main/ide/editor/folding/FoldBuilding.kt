package org.main.ide.editor.folding

import org.antlr.v4.runtime.tree.ParseTree
import org.ide.PluginController.PluginInterface.Plugin

fun buildFoldsFromParseTree(
    root: ParseTree,
    plugin: Plugin
): List<FoldRegion> {

    val result = mutableListOf<FoldRegion>()

    fun visit(node: ParseTree) {
        val keywords = plugin.getKeyWordsOfModule(node)

        if (keywords.isNullOrEmpty()) {
            for (i in 0 until node.childCount) {
                visit(node.getChild(i))
            }
            return
        }

        val bounds = plugin.getBounds(node)
            ?: run {
                for (i in 0 until node.childCount) {
                    visit(node.getChild(i))
                }
                return
            }

        val body = plugin.getPositionOfModuleBody(node)

        val alwaysFold = keywords.any {
            val text = it.text.lowercase()
            text == "if" || text == "while" || text == "until"
        }

        val foldAllowed =
            alwaysFold ||
                    (body != null && body.rowE > body.rowS)

        if (foldAllowed && bounds.rowE > bounds.rowS) {
            result += FoldRegion(
                startLine = bounds.rowS,
                endLine = bounds.rowE,
                collapsed = false
            )
        }

        for (i in 0 until node.childCount) {
            visit(node.getChild(i))
        }
    }

    visit(root)

    return result
        .distinctBy { it.startLine to it.endLine }
        .sortedBy { it.startLine }
}
