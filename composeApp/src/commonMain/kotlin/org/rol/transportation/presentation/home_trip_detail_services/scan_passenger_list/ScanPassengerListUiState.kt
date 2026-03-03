package org.rol.transportation.presentation.home_trip_detail_services.scan_passenger_list

import org.rol.transportation.domain.model.Passenger

data class ScanPassengerListUiState(
    val passengers: List<Passenger> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)
