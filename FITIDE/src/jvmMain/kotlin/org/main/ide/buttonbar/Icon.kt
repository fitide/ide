package org.main.ide.buttonbar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IconWithTooltip(svgResource: DrawableResource, tooltip: String, onClick: () -> Unit = {}) {
    TooltipArea(
        tooltip = {
            Box(
                modifier = Modifier
                    .background(Color(0xFF333333))
                    .padding(6.dp)
            ) {
                Text(text = tooltip, color = Color.White)
            }
        },
        tooltipPlacement = TooltipPlacement.CursorPoint(offset = DpOffset(10.dp, 10.dp))
    ) {
        FilledTonalIconButton(
            onClick = onClick,
            modifier = Modifier.size(48.dp),
            shape = RoundedCornerShape(0.dp),
            colors = IconButtonDefaults.iconButtonColors()
        ) {
            Icon(
                painter = painterResource(svgResource),
                contentDescription = tooltip,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )
        }
    }
}
