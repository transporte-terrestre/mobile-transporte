package org.rol.transportation.presentation.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.rol.transportation.presentation.navigation.Screen

class HomeMenuViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeMenuUiState())
    val uiState: StateFlow<HomeMenuUiState> = _uiState.asStateFlow()

    init {
        loadMenuItems()
    }

    private fun loadMenuItems() {

        val items = listOf(
            MenuItem(
                id = 1,
                title = "CheckList",
                icon = Icons.Default.FactCheck,
                route = Screen.ChecklistTrips(),
                isEnabled = true
            ),
            MenuItem(
                id = 2,
                title = "Mi Programación",
                icon = Icons.Default.Schedule,
                route = Screen.Home,
                isEnabled = true
            ),
            MenuItem(
                id = 3,
                title = "Documentos",
                icon = Icons.Default.Folder,
                route = Screen.DriverDocuments,
                isEnabled = true
            ),
            MenuItem(
                id = 4,
                title = "Alertas",
                icon = Icons.Default.Warning,
                route = Screen.Notifications,
                isEnabled = true
            ),
            MenuItem(
                id = 5,
                title = "Mi Perfil",
                icon = Icons.Default.Person,
                route = Screen.Profile,
                isEnabled = true
            )
        )
        _uiState.update { it.copy(menuItems = items) }
    }
}
