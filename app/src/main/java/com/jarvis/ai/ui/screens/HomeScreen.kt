package com.jarvis.ai.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.jarvis.ai.automation.DeviceStatus
import com.jarvis.ai.data.preferences.AppSettings
import com.jarvis.ai.ui.components.AiOrb
import com.jarvis.ai.ui.components.GlassCard
import kotlinx.coroutines.delay

private val quickCommands = listOf(
    "Chrome kholo", "YouTube open karo", "Flashlight on karo",
    "Battery kitni hai?", "Calculator kholo", "Ek note save karo",
)

@Composable
fun HomeScreen(vm: JarvisViewModel, settings: AppSettings, onOpenChat: () -> Unit) {
    val context = LocalContext.current
    val accent = MaterialTheme.colorScheme.primary
    var listening by remember { mutableStateOf(false) }
    var input by remember { mutableStateOf("") }

    val battery by produceState(DeviceStatus.batteryPercent(context)) {
        while (true) { value = DeviceStatus.batteryPercent(context); delay(5000) }
    }
    val online by produceState(DeviceStatus.isOnline(context)) {
        while (true) { value = DeviceStatus.isOnline(context); delay(5000) }
    }

    val micLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        listening = granted
    }
    fun onMicTap() {
        val granted = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
            PackageManager.PERMISSION_GRANTED
        when {
            listening -> listening = false
            granted -> listening = true
            else -> micLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("JARVIS AI", fontSize = 28.sp, fontWeight = FontWeight.Bold, letterSpacing = 6.sp, color = accent)
        Spacer(Modifier.height(12.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatusChip("BUNNY: Phase 3", Color(0xFFFFB300), Modifier.weight(1f))
            StatusChip("AI: Gemini Phase 2", Color(0xFFFFB300), Modifier.weight(1f))
        }
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatusChip(
                if (battery >= 0) "Battery: $battery%" else "Battery: --",
                Color(0xFF39FF88), Modifier.weight(1f),
            )
            StatusChip(
                if (online) "Internet: Online" else "Internet: Offline",
                if (online) Color(0xFF39FF88) else Color(0xFFFF5252), Modifier.weight(1f),
            )
        }

        Spacer(Modifier.height(20.dp))
        AiOrb(active = listening, animate = settings.animationsEnabled, accent = accent)
        Spacer(Modifier.height(8.dp))
        Text(
            if (listening) "Mic ready • voice recognition Phase 3 mein judega" else "Mic dabaiye ya type kijiye",
            color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp,
        )
        Spacer(Modifier.height(14.dp))
        Box(
            Modifier.size(68.dp).clip(CircleShape).background(accent),
            contentAlignment = Alignment.Center,
        ) {
            IconButton(onClick = { onMicTap() }, modifier = Modifier.size(68.dp)) {
                Icon(if (listening) Icons.Filled.Stop else Icons.Filled.Mic, "Microphone", tint = Color.Black, modifier = Modifier.size(32.dp))
            }
        }

        Spacer(Modifier.height(20.dp))
        GlassCard(Modifier.fillMaxWidth()) {
            Row(Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = input, onValueChange = { input = it },
                    placeholder = { Text("Jarvis se kuch poochiye...") },
                    modifier = Modifier.weight(1f), singleLine = true,
                )
                IconButton(onClick = {
                    if (input.isNotBlank()) { vm.send(input); input = ""; onOpenChat() }
                }) { Icon(Icons.AutoMirrored.Filled.Send, "Send", tint = accent) }
            }
        }

        Spacer(Modifier.height(14.dp))
        Text("Quick Commands", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp, modifier = Modifier.align(Alignment.Start))
        Spacer(Modifier.height(6.dp))
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            quickCommands.forEach { cmd ->
                AssistChip(onClick = { input = cmd }, label = { Text(cmd) })
            }
        }
    }
}

@Composable
private fun StatusChip(text: String, dot: Color, modifier: Modifier = Modifier) {
    GlassCard(modifier, corner = 14.dp) {
        Row(Modifier.padding(horizontal = 12.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(8.dp).clip(CircleShape).background(dot))
            Spacer(Modifier.size(8.dp))
            Text(text, fontSize = 12.sp, maxLines = 1)
        }
    }
}
