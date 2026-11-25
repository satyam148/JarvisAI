package com.jarvis.ai.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jarvis.ai.ui.theme.JarvisColors

/** Glowing animated AI core with rotating rings. [active] speeds it up (listening). */
@Composable
fun AiOrb(
    modifier: Modifier = Modifier,
    size: Dp = 220.dp,
    active: Boolean = false,
    animate: Boolean = true,
    accent: Color,
    secondary: Color = JarvisColors.Purple,
) {
    val transition = rememberInfiniteTransition(label = "orb")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(if (active) 5000 else 14000, easing = LinearEasing)),
        label = "rotation",
    )
    val pulse by transition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            tween(if (active) 700 else 2200, easing = FastOutSlowInEasing),
            RepeatMode.Reverse,
        ),
        label = "pulse",
    )
    val rot = if (animate) rotation else 0f
    val p = if (animate) pulse else 1f

    Canvas(modifier.size(size)) {
        val c = center
        val r = this.size.minDimension / 2f
        // outer glow
        drawCircle(
            brush = Brush.radialGradient(
                listOf(accent.copy(alpha = 0.50f), secondary.copy(alpha = 0.22f), Color.Transparent),
                center = c, radius = r * p,
            ),
            radius = r * p, center = c,
        )
        // core
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color.White, accent, secondary),
                center = c, radius = r * 0.36f * p,
            ),
            radius = r * 0.36f * p, center = c,
        )
        val stroke = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        rotate(rot, pivot = c) {
            val d = r * 1.62f
            drawArc(accent, 0f, 110f, false, Offset(c.x - d / 2, c.y - d / 2), Size(d, d), style = stroke)
            drawArc(accent, 180f, 70f, false, Offset(c.x - d / 2, c.y - d / 2), Size(d, d), style = stroke)
        }
        rotate(-rot * 1.4f, pivot = c) {
            val d = r * 1.28f
            drawArc(secondary, 40f, 90f, false, Offset(c.x - d / 2, c.y - d / 2), Size(d, d), style = stroke)
            drawArc(secondary, 220f, 60f, false, Offset(c.x - d / 2, c.y - d / 2), Size(d, d), style = stroke)
        }
        drawCircle(accent.copy(alpha = 0.35f), radius = r * 0.96f, center = c, style = Stroke(1.dp.toPx()))
    }
}
