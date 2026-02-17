package org.rol.transportation.presentation.checklist_seat_belts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.seat_belts.SeatBeltItem
import org.rol.transportation.domain.usecase.GetSeatBeltsUseCase
import org.rol.transportation.domain.usecase.UpsertSeatBeltsUseCase
import org.rol.transportation.utils.Resource


class SeatBeltsViewModel(
    private val getUseCase: GetSeatBeltsUseCase,
    private val upsertUseCase: UpsertSeatBeltsUseCase,
    private val vehiculoId: Int,
    private val viajeId: Int,
    private val tipo: String,
    private val safeDocumentId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SeatBeltsUiState(
            vehiculoId = vehiculoId,
            viajeId = viajeId,
            viajeTipo = TripType.fromString(tipo) ?: TripType.SALIDA
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        val documentId = if (safeDocumentId == -1) null else safeDocumentId

        viewModelScope.launch {
            getUseCase(vehiculoId, documentId).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true, error = null) }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                inspection = result.data,
                                isLoading = false,
                                hasChanges = false
                            )
                        }
                    }
                    is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }


    // Actualizar Checkbox
    fun onItemChanged(key: String, isEnabled: Boolean) {
        updateItem(key) { it.copy(habilitado = isEnabled) }
    }

    // Actualizar Observación
    fun onObservationChanged(key: String, text: String) {
        updateItem(key) { it.copy(observacion = text) }
    }

    private fun updateItem(key: String, update: (SeatBeltItem) -> SeatBeltItem) {
        val currentInspection = _uiState.value.inspection ?: return
        val currentItems = currentInspection.items.toMutableMap()

        currentItems[key] = update(currentItems[key] ?: return)

        _uiState.update {
            it.copy(
                inspection = currentInspection.copy(items = currentItems),
                hasChanges = true
            )
        }
    }

    fun save() {
        val current = _uiState.value.inspection ?: return
        val tripType = _uiState.value.viajeTipo

        viewModelScope.launch {
            upsertUseCase(vehiculoId, viajeId, tripType, current).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { it.copy(isSaving = true, error = null) }
                    is Resource.Success -> _uiState.update { it.copy(inspection = result.data, isSaving = false, hasChanges = false, successMessage = "Guardado correctamente") }
                    is Resource.Error -> _uiState.update { it.copy(isSaving = false, error = result.message) }
                }
            }
        }
    }

    fun clearMessages() { _uiState.update { it.copy(error = null, successMessage = null) } }
}