package org.rol.transportation.presentation.home_trip

import org.rol.transportation.domain.model.Trip

data class HomeUiState(
    val viajes: List<Trip> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val page: Int = 1,
    val hasMorePages: Boolean = false
)