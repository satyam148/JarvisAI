package com.jarvis.ai.ui.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

 data class ChatMessage(val text: String, val fromUser: Boolean)

class JarvisViewModel : ViewModel() {
    val messages = mutableStateListOf<ChatMessage>()
    var isLoading = androidx.compose.runtime.mutableStateOf(false)
        private set

    fun send(text: String, apiKey: String) {
        val prompt = text.trim()
        if (prompt.isEmpty() || isLoading.value) return
        messages.add(ChatMessage(prompt, fromUser = true))
        if (apiKey.isBlank()) {
            messages.add(ChatMessage("Pehle Settings > AI SETTINGS mein Gemini API key save kijiye.", false))
            return
        }
        isLoading.value = true
        viewModelScope.launch {
            val answer = withContext(Dispatchers.IO) { requestGemini(prompt, apiKey) }
            messages.add(ChatMessage(answer, fromUser = false))
            isLoading.value = false
        }
    }

    private fun requestGemini(prompt: String, apiKey: String): String {
        var connection: HttpURLConnection? = null
        return try {
            val url = URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.8-flash:generateContent?key=${apiKey}")
            connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                connectTimeout = 20000
                readTimeout = 60000
                doOutput = true
                setRequestProperty("Content-Type", "application/json; charset=utf-8")
            }
            val body = JSONObject()
                .put("system_instruction", JSONObject().put("parts", JSONArray().put(JSONObject().put("text", "You are Jarvis, a helpful AI assistant. Reply in the same language as the user, preferably simple Hindi/Hinglish when they use it. Be clear and safe."))))
                .put("contents", JSONArray().put(JSONObject()
                    .put("role", "user")
                    .put("parts", JSONArray().put(JSONObject().put("text", prompt)))))
            connection.outputStream.use { it.write(body.toString().toByteArray(Charsets.UTF_8)) }
            val code = connection.responseCode
            val stream = if (code in 200..299) connection.inputStream else connection.errorStream
            val responseText = stream.bufferedReader().use { it.readText() }
            if (code !in 200..299) {
                val message = runCatching { JSONObject(responseText).getJSONObject("error").getString("message") }.getOrDefault("HTTP $code")
                "Gemini API error ($code): $message"
            } else {
                val json = JSONObject(responseText)
                json.optJSONArray("candidates")?.optJSONObject(0)
                    ?.optJSONObject("content")?.optJSONArray("parts")?.optJSONObject(0)
                    ?.optString("text")?.takeIf { it.isNotBlank() }
                    ?: "Gemini se khaali jawab mila. Dobara koshish karein."
            }
        } catch (e: Exception) {
            "Connection error: ${e.localizedMessage ?: "Internet ya API key check karein."}"
        } finally {
            connection?.disconnect()
        }
    }
}
