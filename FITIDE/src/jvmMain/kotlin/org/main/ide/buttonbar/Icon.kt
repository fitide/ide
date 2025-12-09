package org.main.ide.buttonbar

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.DrawableResource
import androidx.compose.material3.Icon

@Composable
fun IconWithTooltip(
    iconRes: DrawableResource,
    tooltip: String,
    onClick: (() -> Unit)? = null
) {
    var hovered by remember { mutableStateOf(false) }
    var pressed by remember { mutableStateOf(false) }

    val bgColor =
        when {
            pressed -> Color(0xFF56595D)
            hovered -> Color(0xFF4A4D51)
            else -> Color(0xFF3D3F43)
        }

    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val pointer = event.changes.firstOrNull()
                        if (pointer != null) {
                            val pos = pointer.position
                            hovered =
                                pos.x in 0f..size.width.toFloat() &&
                                        pos.y in 0f..size.height.toFloat()
                        }
                    }
                }
            }

            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val down = event.changes.firstOrNull { it.pressed }
                        if (down != null) {
                            down.consume()
                            while (true) {
                                val ev = awaitPointerEvent()
                                val up = ev.changes.firstOrNull()
                                if (up != null && !up.pressed) {
                                    break
                                }
                            }
                        }
                    }
                }
            }

            .combinedClickable(onClick = { onClick?.invoke() }),

        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = tooltip,
            tint = Color(0xFFE6E6E6),
            modifier = Modifier.size(22.dp)
        )
    }
}
