package com.jarvis.ai.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.jarvis.ai.data.preferences.AppSettings
import com.jarvis.ai.ui.components.GlassCard
import com.jarvis.ai.ui.theme.JarvisColors

@Composable
fun SettingsScreen(settings: AppSettings) {
    val context = LocalContext.current
    val accent = MaterialTheme.colorScheme.primary

    fun micGranted() = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    fun notifGranted() = Build.VERSION.SDK_INT < 33 ||
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

    var mic by remember { mutableStateOf(micGranted()) }
    var notif by remember { mutableStateOf(notifGranted()) }
    var apiKeyDraft by remember { mutableStateOf(settings.geminiApiKey) }
    var keySaved by remember { mutableStateOf(settings.geminiApiKey.isNotBlank()) }
    val micLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { mic = it }
    val notifLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { notif = it }

    LazyColumn(Modifier.fillMaxSize(), contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp)) {
        item { Text("Settings", style = MaterialTheme.typography.headlineMedium, color = accent, fontWeight = FontWeight.Bold) }

        item {
            Section("AI SETTINGS") {
                Text("Gemini API key", fontSize = 14.sp)
                OutlinedTextField(
                    value = apiKeyDraft,
                    onValueChange = { apiKeyDraft = it; keySaved = false },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Paste your API key") },
                )
                TextButton(onClick = {
                    settings.updateGeminiApiKey(apiKeyDraft)
                    keySaved = apiKeyDraft.isNotBlank()
                }) { Text("Save API key") }
                Text(if (keySaved) "API key saved on this device" else "API key not saved", fontSize = 12.sp, color = if (keySaved) Color(0xFF66BB6A) else Color(0xFFFFB74D))
                Info("Model: Gemini 3.8 Flash. Chat requires internet access.", "Phase 2")
                Text("Security: This prototype stores the key in app-private preferences. Do not publish this APK or share the key. For a public app, keep the key on a backend server.", fontSize = 12.sp, color = Color(0xFFFFB74D))
            }
        }
        item {
            Section("VOICE SETTINGS") {
                Info("BUNNY voice, Hindi/English, speed, pitch, volume, test voice", "Phase 3")
            }
        }
        item {
            Section("AUTOMATION SETTINGS") {
                PermRow("Microphone", mic, onGrant = {
                    if (!mic) micLauncher.launch(Manifest.permission.RECORD_AUDIO)
                })
                PermRow("Notifications", notif, onGrant = {
                    if (!notif && Build.VERSION.SDK_INT >= 33) notifLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                })
                Info("Accessibility service: tap/swipe automation", "Phase 4")
            }
        }
        item {
            Section("APPEARANCE") {
                Text("Neon theme", fontSize = 14.sp)
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    JarvisColors.accents.forEachIndexed { i, a ->
                        Box(
                            Modifier.size(40.dp).clip(CircleShape).background(a.color)
                                .border(if (i == settings.accentIndex) 3.dp else 0.dp, Color.White, CircleShape)
                                .clickable { settings.updateAccent(i) }
                        )
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(JarvisColors.accents[settings.accentIndex].name, fontSize = 12.sp, color = Color.White.copy(alpha = 0.6f))
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("Animations", Modifier.weight(1f))
                    Switch(settings.animationsEnabled, { settings.updateAnimations(it) })
                }
                Info("AI avatar settings", "Baad mein")
            }
        }
        item {
            Section("PRIVACY") {
                Text(
                    "Abhi tak koi data server par nahi bhejta. Chat sirf is session mein hai aur app band hone par hat jaati hai. " +
                        "Mic sirf tab use hoga jab aap mic dabayenge (Phase 3). Gemini API key secure storage mein rakhi jayegi (Phase 2).",
                    fontSize = 13.sp, color = Color.White.copy(alpha = 0.75f),
                )
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = {
                    context.startActivity(
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.packageName, null))
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                }) { Text("Android app permissions kholo") }
                Info("Memory management, clear history", "Phase 7")
            }
        }
    }
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    Spacer(Modifier.height(20.dp))
    Text(title, fontSize = 12.sp, letterSpacing = 2.sp, color = Color.White.copy(alpha = 0.55f))
    Spacer(Modifier.height(8.dp))
    GlassCard(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp)) { content() } }
}

@Composable
private fun Info(text: String, phase: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text, Modifier.weight(1f), fontSize = 13.sp, color = Color.White.copy(alpha = 0.7f))
        Text(phase, fontSize = 12.sp, color = Color(0xFFFFB300))
    }
}

@Composable
private fun PermRow(name: String, granted: Boolean, onGrant: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
        Text("$name: ${if (granted) "Allowed" else "Not allowed"}", Modifier.weight(1f))
        if (!granted) TextButton(onClick = onGrant) { Text("Allow") }
    }
}
