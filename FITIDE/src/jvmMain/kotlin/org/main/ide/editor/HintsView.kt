package org.main.ide.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ide.LinkTreeController.Tree.ToolClasses.HintNode
import org.main.ide.uistate.UIColors

@Composable
fun HintPopup(
    hints: List<HintNode>,
    selected: Int,
    offset: DpOffset,
    onSelect: (Int) -> Unit,
    onClick: (HintNode) -> Unit
) {
    Box(
        modifier = Modifier
            .offset(offset.x, offset.y)
            .widthIn(min = 160.dp)
            .heightIn(max = 200.dp)
            .background(
                UIColors.HintBg,
                RoundedCornerShape(6.dp)
            )
            .border(
                1.dp,
                UIColors.HintBorder,
                RoundedCornerShape(6.dp)
            )
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            hints.forEachIndexed { index, hint ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (index == selected)
                                UIColors.HintSelectedBg
                            else
                                Color.Transparent
                        )
                        .clickable {
                            onSelect(index)
                            onClick(hint)
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = hint.name,
                        color = UIColors.TextPrimary,
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}