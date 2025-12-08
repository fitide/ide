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
            .background(Color.Transparent)
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

                        Key.Delete,
                        Key.Backspace -> {
                            fileExplorer.deleteSelected()
                            true
                        }

                        else -> false
                    }
                } else {
                    when (event.key) {
                        Key.DirectionDown,
                        Key.DirectionUp -> {
                            val currentTree = fileExplorer.fileTree
                                ?: return@onPreviewKeyEvent false
                            val rootPathStr = fileExplorer.currentProject?.toString()
                                ?: return@onPreviewKeyEvent false
                            if (rootPathStr.isEmpty()) return@onPreviewKeyEvent false

                            val flatList = flattenTree(
                                root = currentTree,
                                rootPath = Path.of(rootPathStr),
                                expanded = fileExplorer.expandedDirs
                            )
                            if (flatList.isEmpty()) return@onPreviewKeyEvent false

                            val delta = if (event.key == Key.DirectionDown) 1 else -1
                            val shift = event.isShiftPressed

                            fun moveSelection(): Boolean {
                                val currentPath = fileExplorer.lastClicked
                                    ?: fileExplorer.selected.firstOrNull()
                                    ?: flatList.first().path

                                val currentIndex = flatList.indexOfFirst { it.path == currentPath }
                                    .let { if (it >= 0) it else 0 }

                                val newIndex = (currentIndex + delta)
                                    .coerceIn(0, flatList.lastIndex)
                                val newPath = flatList[newIndex].path

                                if (!shift) {
                                    fileExplorer.click(newPath, ctrl = false, shift = false)
                                    fileExplorer.selectionAnchor = newPath
                                } else {
                                    val anchorPath = fileExplorer.selectionAnchor
                                        ?: run {
                                            fileExplorer.selectionAnchor = currentPath
                                            currentPath
                                        }

                                    val anchorIndex = flatList.indexOfFirst { it.path == anchorPath }
                                        .let { if (it >= 0) it else currentIndex }

                                    val from = minOf(anchorIndex, newIndex)
                                    val to = maxOf(anchorIndex, newIndex)

                                    val pathsInRange = flatList.subList(from, to + 1)
                                        .map { it.path }
                                        .toSet()

                                    fileExplorer.selected = pathsInRange
                                    fileExplorer.lastClicked = newPath
                                }

                                return true
                            }

                            moveSelection()
                        }

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