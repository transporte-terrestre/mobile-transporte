package org.rol.transportation.presentation.checklist_tools_inspection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.tools_inspection.ToolItem
import org.rol.transportation.domain.usecase.GetToolsInspectionUseCase
import org.rol.transportation.domain.usecase.UpsertToolsInspectionUseCase
import org.rol.transportation.utils.Resource

class ToolsInspectionViewModel(
    private val getUseCase: GetToolsInspectionUseCase,
    private val upsertUseCase: UpsertToolsInspectionUseCase,
    private val vehiculoId: Int,
    private val viajeId: Int,
    private val tipo: String,
    private val vehiculoChecklistDocumentId: Int?
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ToolsInspectionUiState(
            vehiculoId = vehiculoId,
            viajeId = viajeId,
            viajeTipo = TripType.fromString(tipo) ?: TripType.SALIDA
        )
    )
    val uiState = _uiState.asStateFlow()

    init { loadData() }

    private fun loadData() {
        val documentId = vehiculoChecklistDocumentId
        val tripType = _uiState.value.viajeTipo

        viewModelScope.launch {
            getUseCase(vehiculoId, documentId).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true, error = null) }
                    is Resource.Success -> {
                        val data = result.data
                        if (data.viajeTipo != null && data.viajeTipo != tripType.value) {
                            loadEmpty()
                        } else {
                            _uiState.update { it.copy(inspection = data, isLoading = false) }
                        }
                    }
                    is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }

    private fun loadEmpty() {
        viewModelScope.launch {
            getUseCase(vehiculoId, null).collect { result ->
                if (result is Resource.Success) _uiState.update { it.copy(inspection = result.data, isLoading = false) }
            }
        }
    }

    // Actualiza cualquier campo del ToolItem
    fun updateItem(key: String, update: (ToolItem) -> ToolItem) {
        val currentInsp = _uiState.value.inspection ?: return
        val items = currentInsp.items.toMutableMap()
        items[key] = update(items[key] ?: return)

        _uiState.update {
            it.copy(inspection = currentInsp.copy(items = items), hasChanges = true)
        }
    }

    fun save() {
        val current = _uiState.value.inspection ?: return
        val tripType = _uiState.value.viajeTipo
        viewModelScope.launch {
            upsertUseCase(vehiculoId, viajeId, tripType, current).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { it.copy(isSaving = true) }
                    is Resource.Success -> _uiState.update { it.copy(inspection = result.data, isSaving = false, hasChanges = false, successMessage = "Guardado correctamente") }
                    is Resource.Error -> _uiState.update { it.copy(isSaving = false, error = result.message) }
                }
            }
        }
    }

    fun clearMessages() { _uiState.update { it.copy(error = null, successMessage = null) } }
}