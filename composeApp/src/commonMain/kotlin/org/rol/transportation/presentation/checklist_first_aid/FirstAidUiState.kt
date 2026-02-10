package org.rol.transportation.presentation.checklist_first_aid

import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.first_aid.FirstAidInspection

data class FirstAidUiState(
    val vehiculoId: Int = 0,
    val viajeId: Int = 0,
    val viajeTipo: TripType = TripType.SALIDA,
    val inspection: FirstAidInspection? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val hasChanges: Boolean = false
)