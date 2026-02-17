package org.rol.transportation.presentation.checklist_item_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.preat.peekaboo.image.picker.toImageBitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.rol.transportation.domain.model.enums.ChecklistDocumentType
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.usecase.GetChecklistDocumentUseCase
import org.rol.transportation.domain.usecase.UploadImageUseCase
import org.rol.transportation.domain.usecase.UpsertChecklistDocumentUseCase
import org.rol.transportation.utils.ImageCompressor
import org.rol.transportation.utils.Resource
import kotlin.time.Clock

class ChecklistItemDetailViewModel(
    private val getChecklistDocumentUseCase: GetChecklistDocumentUseCase,
    private val upsertChecklistDocumentUseCase: UpsertChecklistDocumentUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
    private val tripId: Int,
    private val checklistItemId: Int,
    private val vehiculoId: Int,
    private val tipo: String,
    private val safeDocumentId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChecklistItemDetailUiState(
        checklistItemId = checklistItemId,
        viajeId = tripId,
        vehiculoId = vehiculoId,
        viajeTipo = TripType.fromString(tipo) ?: TripType.SALIDA,
    ))
    val uiState: StateFlow<ChecklistItemDetailUiState> = _uiState.asStateFlow()

    private var initialPhotoUrl: String? = null

    init {
        val documentType = ChecklistDocumentType.fromChecklistItemId(checklistItemId)
        if (documentType != null) {
            _uiState.update { it.copy(documentType = documentType) }
            loadDocument()
        } else {
            _uiState.update {
                it.copy(error = "Este item no requiere imagen")
            }
        }
    }

    private fun loadDocument() {
        val documentType = _uiState.value.documentType ?: return
        val documentId = if (safeDocumentId == -1) null else safeDocumentId

        viewModelScope.launch {
            getChecklistDocumentUseCase(vehiculoId, documentType, documentId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        val document = result.data

                        val photoUrl = document.document.photo.url.takeIf { it.isNotBlank() }
                        initialPhotoUrl = photoUrl

                        _uiState.update {
                            it.copy(
                                currentPhotoUrl = photoUrl,
                                isLoading = false,
                                error = null,
                                hasChanges = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                currentPhotoUrl = null,
                                isLoading = false,
                                error = result.message,
                                hasChanges = false
                            )
                        }
                    }
                }
            }
        }
    }


    fun onImageSelected(imageBytes: ByteArray) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                // Reduce la imagen drásticamente antes de validarla o guardarla
                val compressedBytes = ImageCompressor.compress(imageBytes)

                // Validación de seguridad (ahora sobre el tamaño comprimido)
                if (compressedBytes.size > 5 * 1024 * 1024) {
                    _uiState.update { it.copy(error = "Imagen demasiado grande", isLoading = false) }
                    return@launch
                }

                val imageBitmap = compressedBytes.toImageBitmap()

                _uiState.update {
                    it.copy(
                        capturedImageBitmap = imageBitmap,
                        capturedImageBytes = compressedBytes,
                        error = null,
                        hasChanges = true,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error: ${e.message}", isLoading = false) }
            }
        }
    }


    fun onImageSelectionError(error: Throwable) {
        _uiState.update {
            it.copy(error = "Error al seleccionar imagen: ${error.message}")
        }
    }

    fun onClearPhoto() {
        _uiState.update {
            it.copy(
                capturedImageBitmap = null,
                capturedImageBytes = null,
                hasChanges = false,
                error = null
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun saveDocument() {
        val documentType = _uiState.value.documentType ?: return
        val imageBytes = _uiState.value.capturedImageBytes ?: return
        val tripType = _uiState.value.viajeTipo

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isSaving = true, error = null) }


                val timestamp = Clock.System.now().toEpochMilliseconds()
                val fileName = "${documentType.endpoint}_${timestamp}.jpg"


                uploadImageUseCase(imageBytes, fileName).collect { uploadResult ->
                    when (uploadResult) {
                        is Resource.Loading -> {
                            // Ya está en estado isSaving
                        }
                        is Resource.Success -> {
                            val uploadedUrl = uploadResult.data.url


                            upsertChecklistDocumentUseCase(
                                vehiculoId = vehiculoId,
                                viajeId = tripId,
                                documentType = documentType,
                                viajeTipo = tripType,
                                photoUrl = uploadedUrl
                            ).collect { result ->
                                when (result) {
                                    is Resource.Loading -> {
                                        // Ya está en estado isSaving
                                    }
                                    is Resource.Success -> {
                                        initialPhotoUrl = result.data.document.photo.url

                                        _uiState.update {
                                            it.copy(
                                                currentPhotoUrl = result.data.document.photo.url,
                                                capturedImageBitmap = null,
                                                capturedImageBytes = null,
                                                isSaving = false,
                                                hasChanges = false,
                                                successMessage = "Imagen guardada correctamente"
                                            )
                                        }

                                    }
                                    is Resource.Error -> {
                                        _uiState.update {
                                            it.copy(
                                                isSaving = false,
                                                error = "Error al guardar documento: ${result.message}"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        is Resource.Error -> {
                            _uiState.update {
                                it.copy(
                                    isSaving = false,
                                    error = "Error al subir imagen: ${uploadResult.message}"
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = "Error inesperado: ${e.message}"
                    )
                }
            }
        }
    }

    fun clearSuccessMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }
}