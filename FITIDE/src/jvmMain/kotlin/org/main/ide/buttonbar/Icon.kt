package org.main.ide.buttonbar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.useResource
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IconWithTooltip(svgPath: String, tooltip: String, onClick: () -> Unit = {}) {
    val painter = useResource(svgPath) { loadSvgPainter(it, Density(1f)) }

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
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                painter = painter,
                contentDescription = tooltip,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )
        }
    }
}