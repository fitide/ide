package org.main.ide.fileexplorer

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import org.ide.IdeController
import java.nio.file.Path

@Composable
fun FileExplorerView(
    ideController: IdeController,
    fileExplorer: FileExplorer
) {
    val tree = fileExplorer.fileTree
    val focusRequester = FocusRequester()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF888888))
            .padding(4.dp)
            .focusRequester(focusRequester)
            .focusable()
            .onPreviewKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false

                val isCopyCutPasteModifier =
                    event.isCtrlPressed || event.isMetaPressed

                if (isCopyCutPasteModifier) {
                    when (event.key) {
                        Key.C -> {
                            fileExplorer.copySelected()
                            true
                        }

                        Key.X -> {
                            fileExplorer.cutSelected()
                            true
                        }

                        Key.V -> {
                            fileExplorer.pasteToSelection()
                            true
                        }

                        else -> false
                    }
                } else {
                    when (event.key) {
                        Key.Delete,
                        Key.Backspace -> {
                            fileExplorer.deleteSelected()
                            true
                        }

                        else -> false
                    }
                }
            }
    ) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        if (tree == null) {
            Text("Проект не открыт", color = Color.LightGray)
            return@Box
        }

        val rootPath = fileExplorer.currentProject?.toString() ?: ""
        if (rootPath.isEmpty()) {
            Text("Проект не открыт", color = Color.White)
            return@Box
        }

        val flatList = flattenTree(
            root = tree,
            rootPath = Path.of(rootPath),
            expanded = fileExplorer.expandedDirs
        )

        LazyColumn {
            items(flatList) { item ->
                FileExplorerNodeView(
                    ideController = ideController,
                    item = item,
                    explorer = fileExplorer
                )
            }
        }
    }
}
