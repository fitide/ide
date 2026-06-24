package org.main.ide.editor.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ide.IdeController
import org.main.ide.uistate.UIColors
import java.nio.file.Path

@Composable
fun EditorTabs(
    ide: IdeController,
    modifier: Modifier = Modifier
) {
    ide.openedFileInfoState().value

    var openFiles: List<Path> = ide.openedFiles

    var currentFile: Path? = ide.openedFilePath

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(34.dp)
            .background(UIColors.Panel),
        verticalAlignment = Alignment.CenterVertically
    ) {
        openFiles.forEach { path ->

            val isActive = path == currentFile
            val isDirty = ide.hasUnsavedChanges(path)

            Row(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        if (isActive)
                            UIColors.PanelElevated
                        else
                            UIColors.Panel
                    )
                    .clickable {
                        ide.changeCurrentFile(path)
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = path.fileName.toString() +
                            if (isDirty) "*" else "",
                    color = UIColors.TextPrimary,
                    fontSize = 13.sp,
                    maxLines = 1
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = "×",
                    color = UIColors.TextSecondary,
                    modifier = Modifier
                        .clickable {
                            ide.closeFile(path);
                        }
                        .padding(start = 4.dp)
                )
            }
        }
    }
}
