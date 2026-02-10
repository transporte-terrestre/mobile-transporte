package org.rol.transportation.presentation.home_trip_detail_services

import org.rol.transportation.data.remote.dto.trip_services.NextStepDto
import org.rol.transportation.data.remote.dto.trip_services.SegmentDto

data class TripServicesUiState (
    val segments: List<SegmentDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAddButtonVisible: Boolean = false,
    val showDialog: Boolean = false,
    val nextStepData: NextStepDto? = null,
    val isCreating: Boolean = false,
    val createSuccess: String? = null
)