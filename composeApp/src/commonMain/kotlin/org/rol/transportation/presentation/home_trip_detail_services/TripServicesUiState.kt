package org.rol.transportation.presentation.home_trip_detail_services

import org.rol.transportation.data.remote.dto.trip_services.ProximoTramoDto
import org.rol.transportation.data.remote.dto.trip_services.SegmentDto

enum class AddOption {
    PROXIMO,
    OCASIONAL,
    DESCANSO
}

data class TripServicesUiState(
    val segments: List<SegmentDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isCreating: Boolean = false,
    val createSuccess: String? = null,
    val currentLocation: org.rol.transportation.domain.model.LocationModel? = null,
    val showGpsDialog: Boolean = false,
    val gpsDisabled: Boolean = false,
    val permissionDenied: Boolean = false,
    val isLocationLoading: Boolean = false,
    val showLocationSheet: Boolean = false,
    val proximoTramoData: ProximoTramoDto? = null,
    val isLoadingProximoTramo: Boolean = false,
    val isLoadingMenuProximo: Boolean = false,
    val showAddMenu: Boolean = false,
    val selectedAddOption: AddOption? = null
) {
    /** El FAB se oculta si ya existe un registro de tipo "destino" */
    val isFabVisible: Boolean
        get() = segments.none { it.tipo.lowercase() == "destino" }
}