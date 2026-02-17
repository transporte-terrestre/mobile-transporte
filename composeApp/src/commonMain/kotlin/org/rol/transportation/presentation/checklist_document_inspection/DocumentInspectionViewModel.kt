package org.rol.transportation.presentation.checklist_document_inspection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.rol.transportation.domain.model.document_inspection.DocumentItem
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.usecase.GetDocumentInspectionUseCase
import org.rol.transportation.domain.usecase.UpsertDocumentInspectionUseCase
import org.rol.transportation.utils.Resource

class DocumentInspectionViewModel(
    private val getUseCase: GetDocumentInspectionUseCase,
    private val upsertUseCase: UpsertDocumentInspectionUseCase,
    private val vehiculoId: Int,
    private val viajeId: Int,
    private val tipo: String,
    private val safeDocumentId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        DocumentInspectionUiState(
            vehiculoId = vehiculoId,
            viajeId = viajeId,
            viajeTipo = TripType.fromString(tipo) ?: TripType.SALIDA
        )
    )
    val uiState: StateFlow<DocumentInspectionUiState> = _uiState.asStateFlow()

    init {
        loadDocuments()
    }

    private fun loadDocuments() {
        val documentId = if (safeDocumentId == -1) null else safeDocumentId

        viewModelScope.launch {
            getUseCase(vehiculoId, documentId).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true, error = null) }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(documentInspection = result.data, isLoading = false, error = null, hasChanges = false)
                        }
                    }
                    is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }

    fun onItemChanged(sectionKey: String, itemKey: String, isEnabled: Boolean) {
        updateItem(sectionKey, itemKey) { it.copy(habilitado = isEnabled) }
    }

    fun onObservationChanged(sectionKey: String, itemKey: String, newObservation: String) {
        updateItem(sectionKey, itemKey) { it.copy(observacion = newObservation) }
    }

    private fun updateItem(sectionKey: String, itemKey: String, update: (DocumentItem) -> DocumentItem) {
        val currentDocs = _uiState.value.documentInspection ?: return
        val currentContent = currentDocs.document

        val updatedContent = when (sectionKey) {
            "documentosVehiculo" -> {
                val newItems = currentContent.documentosVehiculo.items.toMutableMap()
                val item = newItems[itemKey] ?: return
                newItems[itemKey] = update(item)

                currentContent.copy(
                    documentosVehiculo = currentContent.documentosVehiculo.copy(items = newItems)
                )
            }
            "documentosConductor" -> {
                val newItems = currentContent.documentosConductor.items.toMutableMap()
                val item = newItems[itemKey] ?: return
                newItems[itemKey] = update(item)

                currentContent.copy(
                    documentosConductor = currentContent.documentosConductor.copy(items = newItems)
                )
            }
            else -> currentContent
        }

        _uiState.update {
            it.copy(
                documentInspection = currentDocs.copy(document = updatedContent),
                hasChanges = true
            )
        }
    }

    fun saveDocuments() {
        val docs = _uiState.value.documentInspection ?: return
        val tripType = _uiState.value.viajeTipo

        viewModelScope.launch {
            upsertUseCase(
                vehiculoId = vehiculoId,
                viajeId = viajeId,
                viajeTipo = tripType,
                inspection = docs
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { it.copy(isSaving = true, error = null) }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                documentInspection = result.data,
                                isSaving = false,
                                hasChanges = false,
                                successMessage = "Documentos guardados correctamente"
                            )
                        }
                    }
                    is Resource.Error -> _uiState.update { it.copy(isSaving = false, error = result.message) }
                }
            }
        }
    }

    fun clearError() { _uiState.update { it.copy(error = null) } }
    fun clearSuccessMessage() { _uiState.update { it.copy(successMessage = null) } }
}