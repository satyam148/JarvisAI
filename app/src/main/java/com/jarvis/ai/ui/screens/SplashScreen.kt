package com.jarvis.ai.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jarvis.ai.ui.components.AiOrb
import com.jarvis.ai.ui.theme.JarvisColors
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(animate: Boolean, onFinished: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(if (visible) 1f else 0f, tween(1200), label = "splashAlpha")
    LaunchedEffect(Unit) {
        visible = true
        delay(2400)
        onFinished()
    }
    Column(
        Modifier.fillMaxSize().background(JarvisColors.Background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AiOrb(size = 180.dp, active = true, animate = animate, accent = MaterialTheme.colorScheme.primary, modifier = Modifier.alpha(alpha))
        Spacer(Modifier.height(32.dp))
        Text("JARVIS AI", fontSize = 34.sp, fontWeight = FontWeight.Bold, letterSpacing = 8.sp,
            color = MaterialTheme.colorScheme.primary, modifier = Modifier.alpha(alpha))
        Spacer(Modifier.height(8.dp))
        Text("Aapki personal AI assistant", color = Color.White.copy(alpha = 0.6f), modifier = Modifier.alpha(alpha))
    }
}
