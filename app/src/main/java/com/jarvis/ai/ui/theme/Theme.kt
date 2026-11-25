package com.jarvis.ai.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class AccentTheme(val name: String, val color: Color)

object JarvisColors {
    val Background = Color(0xFF05060A)
    val NavSurface = Color(0xFF0B0D16)
    val Purple = Color(0xFF9D4DFF)
    val accents = listOf(
        AccentTheme("Neon Blue", Color(0xFF00E5FF)),
        AccentTheme("Neon Purple", Color(0xFFB66DFF)),
        AccentTheme("Neon Green", Color(0xFF39FF88)),
        AccentTheme("Neon Pink", Color(0xFFFF4DA6)),
    )
}

@Composable
fun JarvisTheme(accent: Color, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = accent,
            onPrimary = Color.Black,
            secondary = JarvisColors.Purple,
            background = JarvisColors.Background,
            onBackground = Color.White,
            surface = JarvisColors.NavSurface,
            onSurface = Color.White,
        ),
        content = content,
    )
}
