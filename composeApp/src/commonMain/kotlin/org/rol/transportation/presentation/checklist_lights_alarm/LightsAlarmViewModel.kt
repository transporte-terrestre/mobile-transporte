package org.rol.transportation.presentation.checklist_lights_alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.usecase.GetLightsAlarmUseCase
import org.rol.transportation.domain.usecase.UpsertLightsAlarmUseCase
import org.rol.transportation.utils.Resource

class LightsAlarmViewModel(
    private val getUseCase: GetLightsAlarmUseCase,
    private val upsertUseCase: UpsertLightsAlarmUseCase,
    private val vehiculoId: Int,
    private val viajeId: Int,
    private val tipo: String,
    private val vehiculoChecklistDocumentId: Int?
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        LightsAlarmUiState(
            vehiculoId = vehiculoId,
            viajeId = viajeId,
            viajeTipo = TripType.fromString(tipo) ?: TripType.SALIDA
        )
    )
    val uiState: StateFlow<LightsAlarmUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        val documentId = vehiculoChecklistDocumentId
        val tripType = _uiState.value.viajeTipo

        viewModelScope.launch {
            getUseCase(vehiculoId, documentId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }
                    is Resource.Success -> {
                        val data = result.data

                        if (data.viajeTipo != null && data.viajeTipo != tripType.value) {
                            loadEmpty()
                        } else {
                            _uiState.update {
                                it.copy(inspection = data, isLoading = false, hasChanges = false)
                            }
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(isLoading = false, error = result.message) }
                    }
                }
            }
        }
    }

    private fun loadEmpty() {
        viewModelScope.launch {
            getUseCase(vehiculoId, null).collect { result ->
                if (result is Resource.Success) {
                    _uiState.update {
                        it.copy(inspection = result.data, isLoading = false, hasChanges = false)
                    }
                }
            }
        }
    }

    fun onItemChanged(key: String, isEnabled: Boolean) {
        val currentInspection = _uiState.value.inspection ?: return
        val currentItems = currentInspection.items.toMutableMap()

        currentItems[key] = currentItems[key]?.copy(estado = isEnabled) ?: return

        _uiState.update {
            it.copy(
                inspection = currentInspection.copy(items = currentItems),
                hasChanges = true
            )
        }
    }

    fun onObservationChanged(key: String, newObservation: String) {
        val currentInspection = _uiState.value.inspection ?: return
        val currentItems = currentInspection.items.toMutableMap()

        currentItems[key] = currentItems[key]?.copy(observacion = newObservation) ?: return

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
            upsertUseCase(
                vehiculoId = vehiculoId,
                viajeId = viajeId,
                viajeTipo = tripType,
                inspection = current
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { it.copy(isSaving = true, error = null) }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                inspection = result.data,
                                isSaving = false,
                                hasChanges = false,
                                successMessage = "Luces y alarmas guardadas correctamente"
                            )
                        }
                    }
                    is Resource.Error -> _uiState.update { it.copy(isSaving = false, error = result.message) }
                }
            }
        }
    }

    fun clearMessages() { _uiState.update { it.copy(error = null, successMessage = null) } }
}