package org.rol.transportation.presentation.home_trip_detail_checklist

import org.rol.transportation.domain.model.ChecklistItemDetail
import org.rol.transportation.domain.model.TripChecklist
import org.rol.transportation.domain.model.enums.ChecklistType

data class ChecklistUiState(
    val checklistItems: List<ChecklistItemDetail> = emptyList(),
    val observaciones: String = "",
    val isLoading: Boolean = false,
    val isValidating: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val existingChecklist: TripChecklist? = null,
    val tipo: ChecklistType = ChecklistType.SALIDA,
    val vehiculoId: Int = 0
)