package com.jarvis.ai.ui.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jarvis.ai.data.preferences.AppSettings
import com.jarvis.ai.ui.screens.AutomationScreen
import com.jarvis.ai.ui.screens.ChatScreen
import com.jarvis.ai.ui.screens.CodingStudioScreen
import com.jarvis.ai.ui.screens.HomeScreen
import com.jarvis.ai.ui.screens.JarvisViewModel
import com.jarvis.ai.ui.screens.SettingsScreen
import com.jarvis.ai.ui.screens.SplashScreen
import com.jarvis.ai.ui.theme.JarvisColors

enum class Dest(val route: String, val label: String, val icon: ImageVector) {
    Home("home", "Home", Icons.Filled.Home),
    Chat("chat", "Chat", Icons.Filled.Chat),
    Coding("coding", "Coding", Icons.Filled.Code),
    Automation("automation", "Automation", Icons.Filled.Bolt),
    Settings("settings", "Settings", Icons.Filled.Settings),
}

@Composable
fun JarvisApp(settings: AppSettings) {
    var showSplash by rememberSaveable { mutableStateOf(true) }
    val vm: JarvisViewModel = viewModel()
    Box(Modifier.fillMaxSize().background(JarvisColors.Background)) {
        Crossfade(targetState = showSplash, label = "splash") { splash ->
            if (splash) SplashScreen(settings.animationsEnabled) { showSplash = false }
            else MainScaffold(vm, settings)
        }
    }
}

@Composable
private fun MainScaffold(vm: JarvisViewModel, settings: AppSettings) {
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val current = backStack?.destination?.route

    fun go(route: String) = nav.navigate(route) {
        popUpTo(nav.graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }

    Scaffold(
        containerColor = JarvisColors.Background,
        bottomBar = {
            NavigationBar(containerColor = JarvisColors.NavSurface) {
                Dest.entries.forEach { d ->
                    NavigationBarItem(
                        selected = current == d.route,
                        onClick = { go(d.route) },
                        icon = { Icon(d.icon, d.label) },
                        label = { Text(d.label) },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.White.copy(alpha = 0.10f),
                        ),
                    )
                }
            }
        },
    ) { inner ->
        NavHost(nav, startDestination = Dest.Home.route, modifier = Modifier.padding(inner).statusBarsPadding()) {
            composable(Dest.Home.route) { HomeScreen(vm, settings, onOpenChat = { go(Dest.Chat.route) }) }
            composable(Dest.Chat.route) { ChatScreen(vm) }
            composable(Dest.Coding.route) { CodingStudioScreen() }
            composable(Dest.Automation.route) { AutomationScreen() }
            composable(Dest.Settings.route) { SettingsScreen(settings) }
        }
    }
}
