package org.rol.transportation.presentation.home_trip_detail_services.register_departure

import org.rol.transportation.data.remote.dto.trip_services.NextStepDto
import org.rol.transportation.domain.model.LocationModel

data class RegisterDepartureUiState(
    val isLoading: Boolean = false,
    val nextStepData: NextStepDto? = null,
    val currentLocation: LocationModel? = null,
    val isRegistering: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val showGpsDialog: Boolean = false,
    val gpsDisabled: Boolean = false,
    val permissionDenied: Boolean = false,
    val isLocationLoading: Boolean = false
)
