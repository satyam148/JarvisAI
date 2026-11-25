package com.jarvis.ai.data.preferences

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class SettingsStore(context: Context) {
    private val prefs = context.getSharedPreferences("jarvis_settings", Context.MODE_PRIVATE)

    var accentIndex: Int
        get() = prefs.getInt("accent", 0)
        set(v) = prefs.edit().putInt("accent", v).apply()

    var animations: Boolean
        get() = prefs.getBoolean("animations", true)
        set(v) = prefs.edit().putBoolean("animations", v).apply()

    var geminiApiKey: String
        get() = prefs.getString("gemini_api_key", "") ?: ""
        set(v) = prefs.edit().putString("gemini_api_key", v.trim()).apply()
}

/** Compose-observable wrapper around [SettingsStore]. */
class AppSettings(private val store: SettingsStore) {
    var accentIndex by mutableIntStateOf(store.accentIndex.coerceIn(0, 3))
        private set
    var animationsEnabled by mutableStateOf(store.animations)
        private set
    var geminiApiKey by mutableStateOf(store.geminiApiKey)
        private set

    fun updateAccent(index: Int) {
        accentIndex = index
        store.accentIndex = index
    }

    fun updateAnimations(enabled: Boolean) {
        animationsEnabled = enabled
        store.animations = enabled
    }

    fun updateGeminiApiKey(key: String) {
        geminiApiKey = key.trim()
        store.geminiApiKey = key.trim()
    }
}
