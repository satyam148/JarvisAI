package com.jarvis.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jarvis.ai.data.preferences.AppSettings
import com.jarvis.ai.data.preferences.SettingsStore
import com.jarvis.ai.ui.navigation.JarvisApp
import com.jarvis.ai.ui.theme.JarvisColors
import com.jarvis.ai.ui.theme.JarvisTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val settings = AppSettings(SettingsStore(applicationContext))
        setContent {
            JarvisTheme(accent = JarvisColors.accents[settings.accentIndex].color) {
                JarvisApp(settings)
            }
        }
    }
}
