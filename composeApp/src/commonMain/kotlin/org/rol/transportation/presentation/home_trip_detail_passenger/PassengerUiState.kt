package org.rol.transportation.presentation.home_trip_detail_passenger

import org.rol.transportation.domain.model.Passenger

data class PassengerUiState(
    val passengers: List<Passenger> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val hasChanges: Boolean = false
)