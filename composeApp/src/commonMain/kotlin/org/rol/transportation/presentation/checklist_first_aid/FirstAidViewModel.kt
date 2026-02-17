package org.rol.transportation.presentation.checklist_first_aid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.first_aid.FirstAidItem
import org.rol.transportation.domain.usecase.GetFirstAidUseCase
import org.rol.transportation.domain.usecase.UpsertFirstAidUseCase
import org.rol.transportation.utils.Resource


class FirstAidViewModel(
    private val getUseCase: GetFirstAidUseCase,
    private val upsertUseCase: UpsertFirstAidUseCase,
    private val vehiculoId: Int,
    private val viajeId: Int,
    private val tipo: String,
    private val safeDocumentId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        FirstAidUiState(
            vehiculoId = vehiculoId, viajeId = viajeId,
            viajeTipo = TripType.fromString(tipo) ?: TripType.SALIDA
        )
    )
    val uiState = _uiState.asStateFlow()

    init { loadData() }

    private fun loadData() {
        val documentId = if (safeDocumentId == -1) null else safeDocumentId

        viewModelScope.launch {
            getUseCase(vehiculoId, documentId).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true, error = null) }
                    is Resource.Success -> {
                        _uiState.update { it.copy(inspection = result.data, isLoading = false) }
                    }
                    is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }


    // Actualizar Ubicación
    fun onLocationChanged(newLocation: String) {
        val current = _uiState.value.inspection ?: return
        _uiState.update { it.copy(inspection = current.copy(location = newLocation), hasChanges = true) }
    }

    // Actualizar Checkbox
    fun onItemChanged(key: String, isEnabled: Boolean) {
        updateItem(key) { it.copy(habilitado = isEnabled) }
    }

    // Actualizar Fechas (Desde el diálogo)
    fun onDatesChanged(key: String, vencimiento: String, salida: String, reposicion: String) {
        updateItem(key) { it.copy(fechaVencimiento = vencimiento, fechaSalida = salida, fechaReposicion = reposicion) }
    }

    private fun updateItem(key: String, update: (FirstAidItem) -> FirstAidItem) {
        val current = _uiState.value.inspection ?: return
        val newItems = current.items.toMutableMap()
        newItems[key] = update(newItems[key] ?: return)
        _uiState.update { it.copy(inspection = current.copy(items = newItems), hasChanges = true) }
    }

    fun save() {
        val current = _uiState.value.inspection ?: return
        val type = _uiState.value.viajeTipo
        viewModelScope.launch {
            upsertUseCase(vehiculoId, viajeId, type, current).collect { result ->
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