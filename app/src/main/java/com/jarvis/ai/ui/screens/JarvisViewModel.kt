package com.jarvis.ai.ui.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class ChatMessage(val text: String, val fromUser: Boolean)

class JarvisViewModel : ViewModel() {
    val messages = mutableStateListOf<ChatMessage>()

    /** Phase 1: stores the message; Gemini replies arrive in Phase 2. */
    fun send(text: String) {
        val t = text.trim()
        if (t.isEmpty()) return
        messages.add(ChatMessage(t, fromUser = true))
        messages.add(
            ChatMessage("Gemini abhi connect nahi hai. AI jawab Phase 2 mein aayenge.", fromUser = false)
        )
    }
}
