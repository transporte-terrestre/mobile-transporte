package org.rol.transportation.presentation.checklist_document_inspection

import org.rol.transportation.domain.model.document_inspection.DocumentInspection
import org.rol.transportation.domain.model.enums.TripType

data class DocumentInspectionUiState(
    val vehiculoId: Int = 0,
    val viajeId: Int = 0,
    val viajeTipo: TripType = TripType.SALIDA,
    val documentInspection: DocumentInspection? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val hasChanges: Boolean = false
)