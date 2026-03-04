package org.rol.transportation.presentation.home_trip_detail_services

import org.rol.transportation.data.remote.dto.trip_services.NextStepDto
import org.rol.transportation.data.remote.dto.trip_services.SegmentDto
import org.rol.transportation.domain.model.LocationModel

data class TripServicesUiState (
    val segments: List<SegmentDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAddButtonVisible: Boolean = false,
    val showDialog: Boolean = false,
    val nextStepData: NextStepDto? = null,
    val isCreating: Boolean = false,
    val createSuccess: String? = null,
    val currentLocation: LocationModel? = null,
    val showGpsDialog: Boolean = false,
    val gpsDisabled: Boolean = false,
    val permissionDenied: Boolean = false,
    val isLocationLoading: Boolean = false,
    val editingSegment: SegmentDto? = null,
    val isDeletingSegment: Boolean = false
)