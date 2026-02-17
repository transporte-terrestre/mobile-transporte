package org.rol.transportation.presentation.home

data class HomeMenuUiState(
    val menuItems: List<MenuItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)