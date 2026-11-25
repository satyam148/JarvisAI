package com.jarvis.ai.ui.screens

import androidx.compose.runtime.Composable
import com.jarvis.ai.ui.components.FeaturePlaceholder

@Composable
fun AutomationScreen() = FeaturePlaceholder(
    title = "Automation",
    phase = "Phase 4 aur 5",
    items = listOf(
        "Apps kholna / search karna (Phase 4)",
        "AccessibilityService: tap, swipe, scroll (Phase 4, aapki permission ke baad)",
        "Flashlight, volume, device info (Phase 5)",
        "YouTube search aur open (Phase 5)",
        "Sensitive actions se pehle confirmation",
    ),
)
