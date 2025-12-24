package org.main.ide.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ide.IdeController
import org.main.ide.editor.folding.FoldRegion
import org.main.ide.uistate.UIColors

@Composable
fun LineNumberGutter(
    visibleToOriginal: IntArray,
    folds: List<FoldRegion>,
    onToggleFold: (Int) -> Unit,
    lineHeight: TextUnit,
    topPadding: Dp,
    modifier: Modifier = Modifier,
    ide : IdeController
) {
    val density = LocalDensity.current

    Column(
        modifier = modifier
            .width(64.dp)
            .background(UIColors.EditorBg)
            .padding(top = topPadding)
    ) {
        if (ide.openedFiles.isEmpty()) {
            return
        }

        repeat(visibleToOriginal.size) { visibleIndex ->
            val originalLine = visibleToOriginal[visibleIndex]
            val fold = folds.find { it.startLine == originalLine }

            Row(
                modifier = Modifier
                    .height(with(density) { lineHeight.toDp() })
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                if (fold != null) {
                    Icon(
                        imageVector =
                            if (fold.collapsed)
                                Icons.AutoMirrored.Filled.KeyboardArrowRight
                            else
                                Icons.Default.KeyboardArrowDown,
                        contentDescription = "Toggle Fold",
                        tint = UIColors.TextSecondary,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable { onToggleFold(originalLine) }
                    )
                } else {
                    Spacer(modifier = Modifier.size(16.dp))
                }

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = (originalLine + 1).toString(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    color = UIColors.TextSecondary,
                    maxLines = 1,
                    modifier = Modifier.padding(end = 4.dp)
                )
            }
        }
    }
}
