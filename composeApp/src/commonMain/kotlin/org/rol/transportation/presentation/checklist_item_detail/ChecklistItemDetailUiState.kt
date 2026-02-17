package org.rol.transportation.presentation.checklist_item_detail

import androidx.compose.ui.graphics.ImageBitmap
import org.rol.transportation.domain.model.enums.ChecklistDocumentType
import org.rol.transportation.domain.model.enums.TripType

data class ChecklistItemDetailUiState(
    val checklistItemId: Int = 0,
    val vehiculoId: Int = 0,
    val viajeId: Int = 0,
    val viajeTipo: TripType = TripType.SALIDA,
    val documentType: ChecklistDocumentType? = null,
    val capturedImageBitmap: ImageBitmap? = null,
    val capturedImageBytes: ByteArray? = null, //  Para guardar los bytes
    val currentPhotoUrl: String? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val hasChanges: Boolean = false
)