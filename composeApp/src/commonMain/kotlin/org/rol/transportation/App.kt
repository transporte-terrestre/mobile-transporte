package org.rol.transportation

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import org.rol.transportation.presentation.navigation.AppNavigation
import org.rol.transportation.presentation.theme.TransportationTheme
import org.rol.transportation.utils.SetStatusBarColor


@Composable
fun App() {

    val statusBarColor = Color(0xFFFFC107)
    SetStatusBarColor(color = statusBarColor, darkIcons = false)

    TransportationTheme {
        AppNavigation()
    }
}