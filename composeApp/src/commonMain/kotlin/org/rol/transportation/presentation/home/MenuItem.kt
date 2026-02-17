package org.rol.transportation.presentation.home

import androidx.compose.ui.graphics.vector.ImageVector
import org.rol.transportation.presentation.navigation.Screen

data class MenuItem(
    val id: Int,
    val title: String,
    val icon: ImageVector,
    val route: Screen?,
    val isEnabled: Boolean = true
)