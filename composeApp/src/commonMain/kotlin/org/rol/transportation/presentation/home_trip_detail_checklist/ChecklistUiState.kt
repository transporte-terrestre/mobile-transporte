package org.rol.transportation.presentation.home_trip_detail_checklist

import org.rol.transportation.domain.model.ChecklistItemDetail
import org.rol.transportation.domain.model.TripChecklist
import org.rol.transportation.domain.model.enums.ChecklistType

data class ChecklistUiState(
    val checklistItems: List<ChecklistItemDetail> = emptyList(),
    val itemsBySections: Map<String, List<ChecklistItemDetail>> = emptyMap(),
    val selectedItems: Map<Int, Boolean> = emptyMap(),
    val observaciones: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false,
    val existingChecklist: TripChecklist? = null,
    val tipo: ChecklistType = ChecklistType.SALIDA,
    val hasChanges: Boolean = false
)