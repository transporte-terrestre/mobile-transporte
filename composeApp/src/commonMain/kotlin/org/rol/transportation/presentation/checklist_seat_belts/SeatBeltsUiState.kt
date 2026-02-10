package org.rol.transportation.presentation.checklist_seat_belts

import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.seat_belts.SeatBeltsInspection

data class SeatBeltsUiState(
    val vehiculoId: Int = 0,
    val viajeId: Int = 0,
    val viajeTipo: TripType = TripType.SALIDA,
    val inspection: SeatBeltsInspection? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val hasChanges: Boolean = false
)