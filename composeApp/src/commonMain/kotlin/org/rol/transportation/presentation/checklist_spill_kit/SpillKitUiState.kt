package org.rol.transportation.presentation.checklist_spill_kit

import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.spill_kit.SpillKitInspection

data class SpillKitUiState(
    val vehiculoId: Int = 0,
    val viajeId: Int = 0,
    val viajeTipo: TripType = TripType.SALIDA,
    val inspection: SpillKitInspection? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val hasChanges: Boolean = false
)