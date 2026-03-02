package org.rol.transportation.presentation.home_trip_detail_services.register_rest

import org.rol.transportation.data.remote.dto.trip_services.NextStepDto
import org.rol.transportation.domain.model.LocationModel

data class RegisterRestUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val nextStepData: NextStepDto? = null,
    val isRegistering: Boolean = false,
    val successMessage: String? = null,
    val currentLocation: LocationModel? = null,
    val showGpsDialog: Boolean = false,
    val gpsDisabled: Boolean = false,
    val permissionDenied: Boolean = false,
    val isLocationLoading: Boolean = false
)
