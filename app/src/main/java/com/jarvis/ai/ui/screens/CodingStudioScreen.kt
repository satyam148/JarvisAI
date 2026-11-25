package com.jarvis.ai.ui.screens

import androidx.compose.runtime.Composable
import com.jarvis.ai.ui.components.FeaturePlaceholder

@Composable
fun CodingStudioScreen() = FeaturePlaceholder(
    title = "Coding Studio",
    phase = "Phase 6",
    items = listOf(
        "Code generate / explain / debug (Gemini)",
        "HTML, CSS, JS, Python, Java, Kotlin, C, C++",
        "Syntax highlighting code editor",
        "Project workspace (Storage Access Framework)",
        "Code kabhi automatically run nahi hoga",
    ),
)
