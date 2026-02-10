package org.rol.transportation.presentation.checklist_tools_inspection

import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.tools_inspection.ToolsInspection

data class ToolsInspectionUiState(
    val vehiculoId: Int = 0,
    val viajeId: Int = 0,
    val viajeTipo: TripType = TripType.SALIDA,
    val inspection: ToolsInspection? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val hasChanges: Boolean = false
)