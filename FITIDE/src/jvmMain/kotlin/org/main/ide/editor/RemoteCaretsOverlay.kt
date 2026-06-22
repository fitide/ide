package org.main.ide.editor

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.ide.IdeController
import kotlin.math.abs

@Composable
fun RemoteCaretsOverlay(
    carets: List<IdeController.RemoteCaret>,
    layout: TextLayoutResult?,
    text: String,
    transform: VisualTransformation,
    lineHeight: TextUnit,
    modifier: Modifier = Modifier,
) {
    if (layout == null) return

    val density = LocalDensity.current
    val measurer = rememberTextMeasurer()
    val scope = rememberCoroutineScope()

    val anims = remember { mutableStateMapOf<String, Animatable<Offset, AnimationVector2D>>() }

    val lineHeightPx = with(density) { lineHeight.toDp().toPx() }
    val caretWidthPx = with(density) { 2.dp.toPx() }

    val transformed = remember(text, transform) { transform.filter(AnnotatedString(text)) }

    LaunchedEffect(carets, layout, transformed) {
        val seen = HashSet<String>()

        for (caret in carets) {
            seen.add(caret.name)

            val originalOffset = offsetFor(caret.line, caret.column, text)
            val mapped = transformed.offsetMapping
                .originalToTransformed(originalOffset)
                .coerceIn(0, layout.layoutInput.text.length)
            val rect = layout.getCursorRect(mapped)
            val target = Offset(rect.left, rect.top)

            val anim = anims[caret.name]
            if (anim == null) {
                anims[caret.name] = Animatable(target, Offset.VectorConverter)
            } else {
                scope.launch {
                    val far = abs(anim.targetValue.y - target.y) > lineHeightPx * 6f
                    if (far) {
                        anim.snapTo(target)
                    } else {
                        anim.animateTo(target, tween(durationMillis = 110, easing = FastOutSlowInEasing))
                    }
                }
            }
        }

        anims.keys.retainAll(seen)
    }

    Canvas(modifier) {
        val length = layout.layoutInput.text.length
        for (caret in carets) {
            val color = colorForUser(caret.name)
            val caretOffset = transformed.offsetMapping
                .originalToTransformed(offsetFor(caret.line, caret.column, text))
                .coerceIn(0, length)
            val caretRect = layout.getCursorRect(caretOffset)
            val p = anims[caret.name]?.value ?: Offset(caretRect.left, caretRect.top)

            caret.selection?.let { sel ->
                try {
                    val start = transformed.offsetMapping
                        .originalToTransformed(offsetFor(sel.startLine, sel.startColumn, text))
                        .coerceIn(0, length)
                    val end = transformed.offsetMapping
                        .originalToTransformed(offsetFor(sel.endLine, sel.endColumn, text))
                        .coerceIn(0, length)
                    if (end > start) {
                        val fill = color.copy(alpha = 0.3f)
                        val firstLine = layout.getLineForOffset(start)
                        val lastLine = layout.getLineForOffset(end)
                        for (ln in firstLine..lastLine) {
                            val left = if (ln == firstLine) layout.getHorizontalPosition(start, true)
                                       else layout.getLineLeft(ln)
                            val right = if (ln == lastLine) layout.getHorizontalPosition(end, true)
                                        else layout.getLineRight(ln)
                            val top = layout.getLineTop(ln)
                            val bottom = layout.getLineBottom(ln)
                            drawRect(fill, topLeft = Offset(left, top),
                                size = Size((right - left).coerceAtLeast(0f), bottom - top))
                        }
                    }
                } catch (ex: Throwable) {
                    // layout/selection out of sync for a frame — skip drawing this selection
                }
            }

            drawLine(
                color = color,
                start = Offset(p.x, p.y),
                end = Offset(p.x, p.y + lineHeightPx),
                strokeWidth = caretWidthPx
            )

            val label = measurer.measure(
                AnnotatedString(caret.name),
                style = TextStyle(fontSize = 12.sp, color = Color.White, fontFamily = FontFamily.SansSerif)
            )
            val padX = 4f
            val padY = 2f
            val flagWidth = label.size.width + padX * 2
            val flagHeight = label.size.height + padY * 2
            val flagTop = (p.y - flagHeight).coerceAtLeast(0f)

            drawRoundRect(
                color = color,
                topLeft = Offset(p.x, flagTop),
                size = Size(flagWidth, flagHeight),
                cornerRadius = CornerRadius(3f, 3f)
            )
            drawText(label, topLeft = Offset(p.x + padX, flagTop + padY))
        }
    }
}

fun colorForUser(name: String): Color {
    val hue = ((name.hashCode() % 360) + 360) % 360
    return Color.hsl(hue.toFloat(), 0.65f, 0.60f)
}