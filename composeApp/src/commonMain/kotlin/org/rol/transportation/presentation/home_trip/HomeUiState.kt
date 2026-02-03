package org.rol.transportation.presentation.home_trip

data class HomeUiState(
    val isRefreshing: Boolean = false,
    val error: String? = null
)