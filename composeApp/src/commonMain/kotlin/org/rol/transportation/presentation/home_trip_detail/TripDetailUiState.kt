package org.rol.transportation.presentation.home_trip_detail

import org.rol.transportation.domain.model.Trip

data class TripDetailUiState (
    val viaje: Trip? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasDepartureStarted: Boolean = false,
    val hasArrivalStarted: Boolean = false
)