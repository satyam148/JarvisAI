package com.jarvis.ai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.jarvis.ai.data.preferences.SettingsStore

@Composable
fun ChatScreen(vm: JarvisViewModel) {
    val accent = MaterialTheme.colorScheme.primary
    val context = LocalContext.current
    val apiKey = remember { SettingsStore(context).geminiApiKey }
    val listState = rememberLazyListState()
    var input by remember { mutableStateOf("") }

    LaunchedEffect(vm.messages.size) {
        if (vm.messages.isNotEmpty()) listState.animateScrollToItem(vm.messages.size - 1)
    }

    Column(Modifier.fillMaxSize().imePadding()) {
        Text("Chat", style = MaterialTheme.typography.headlineMedium, color = accent, modifier = Modifier.padding(20.dp, 8.dp))
        if (vm.messages.isEmpty()) {
            Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("Abhi koi message nahi. Kuch likhiye!", color = Color.White.copy(alpha = 0.5f))
            }
        } else {
            LazyColumn(
                Modifier.weight(1f).fillMaxWidth(), state = listState,
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                itemsIndexed(vm.messages) { _, m ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = if (m.fromUser) Arrangement.End else Arrangement.Start) {
                        Box(
                            Modifier.clip(RoundedCornerShape(16.dp))
                                .background(if (m.fromUser) accent.copy(alpha = 0.85f) else Color.White.copy(alpha = 0.10f))
                                .padding(12.dp)
                        ) {
                            Text(m.text, color = if (m.fromUser) Color.Black else Color.White)
                        }
                    }
                }
            }
        }
        if (vm.isLoading.value) {
            Text("Jarvis soch raha hai...", color = accent, modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
        }
        Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = input, onValueChange = { input = it },
                placeholder = { Text("Message likhiye...") },
                modifier = Modifier.weight(1f), maxLines = 4,
            )
            IconButton(onClick = { vm.send(input, apiKey); input = "" }, enabled = !vm.isLoading.value) {
                Icon(Icons.AutoMirrored.Filled.Send, "Send", tint = accent)
            }
        }
    }
}
