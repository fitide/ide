package org.main.ide.fileexplorer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import ide.fitide.generated.resources.*
import org.ide.FileExplorerController.Node.Directory
import org.ide.FileExplorerController.Node.FEFile
import org.ide.FileExplorerController.Node.Node
import org.ide.IdeController
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import java.nio.file.Path
import javax.swing.JOptionPane

@Composable
fun FileExplorerNodeView(
    ideController: IdeController,
    item: FlatNode,
    explorer: FileExplorer
) {
    val node: Node = item.node
    val isDirectory = node is Directory
    val isSelected = explorer.selected.contains(item.path)
    val isExpanded = explorer.expandedDirs.contains(item.path)

    var menuVisible by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isSelected) Color(0xFF3A3A3A) else Color.Transparent)
            .padding(start = (item.depth * 16).dp, top = 2.dp, bottom = 2.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                explorer.click(item.path, ctrl = false, shift = false)

                if (!isDirectory) {
                    ideController.openFile(Path.of(item.path))
                }
            }
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        if (event.buttons.isSecondaryPressed) menuVisible = true
                    }
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isDirectory) {
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.ChevronRight,
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                tint = Color.White,
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 2.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        explorer.toggleExpand(item.path)
                    }
            )
        } else {
            Spacer(Modifier.width(20.dp))
        }

        if (isDirectory) {
            Icon(
                painter = painterResource(Res.drawable.folder),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Icon(
                painter = FileIconForExtension(node as FEFile),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(6.dp))

        Text(
            text = node.name,
            color = Color.White
        )

        DropdownMenu(expanded = menuVisible, onDismissRequest = { menuVisible = false }) {
            DropdownMenuItem(
                text = { Text("Новый файл") },
                onClick = {
                    val name = JOptionPane.showInputDialog("Имя файла:")?.trim()
                    if (!name.isNullOrEmpty()) {
                        val targetDir =
                            if (isDirectory) Path.of(item.path)
                            else Path.of(item.path).parent ?: Path.of(item.path)
                        explorer.createFile(targetDir, name)
                    }
                    menuVisible = false
                }
            )

            DropdownMenuItem(
                text = { Text("Новая папка") },
                onClick = {
                    val name = JOptionPane.showInputDialog("Имя папки:")?.trim()
                    if (!name.isNullOrEmpty()) {
                        val targetDir =
                            if (isDirectory) Path.of(item.path)
                            else Path.of(item.path).parent ?: Path.of(item.path)
                        explorer.createDirectory(targetDir, name)
                    }
                    menuVisible = false
                }
            )

            DropdownMenuItem(
                text = { Text("Переименовать") },
                onClick = {
                    val currentName = node.name
                    val name = JOptionPane.showInputDialog(
                        null,
                        "Новое имя:",
                        currentName
                    )?.trim()
                    if (!name.isNullOrEmpty() && name != currentName) {
                        explorer.rename(Path.of(item.path), name, isDirectory)
                    }
                    menuVisible = false
                }
            )

            DropdownMenuItem(
                text = { Text("Удалить") },
                onClick = {
                    explorer.delete(Path.of(item.path), isDirectory)
                    menuVisible = false
                }
            )

            DropdownMenuItem(
                text = { Text("Копировать") },
                onClick = {
                    explorer.copySelected()
                    menuVisible = false
                }
            )

            DropdownMenuItem(
                text = { Text("Вырезать") },
                onClick = {
                    explorer.cutSelected()
                    menuVisible = false
                }
            )

            DropdownMenuItem(
                text = { Text("Вставить") },
                onClick = {
                    val target = if (isDirectory) Path.of(item.path)
                    else Path.of(item.path).parent ?: Path.of(item.path)
                    explorer.paste(target)
                    menuVisible = false
                }
            )
        }
    }
}

@Composable
fun FileIconForExtension(file: FEFile): androidx.compose.ui.graphics.painter.Painter {
    val ext = file.name.substringAfterLast(".", "").lowercase()

    val res: DrawableResource = when (ext) {
        "kt" -> Res.drawable.kotlin
        "java" -> Res.drawable.java
        "go" -> Res.drawable.go
        "py" -> Res.drawable.python
        "c" -> Res.drawable.c
        "cpp" -> Res.drawable.cpp
        "clj" -> Res.drawable.clojure
        "asm" -> Res.drawable.assembly
        "txt" -> Res.drawable.txt
        "md" -> Res.drawable.markdown
        "json" -> Res.drawable.json
        else -> Res.drawable.file
    }

    return painterResource(res)
}
