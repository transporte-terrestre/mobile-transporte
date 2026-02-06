package org.rol.transportation.presentation.home_trip_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.rol.transportation.domain.model.enums.ChecklistType
import org.rol.transportation.domain.usecase.GetTripChecklistUseCase
import org.rol.transportation.domain.usecase.GetTripDetailUseCase
import org.rol.transportation.utils.Resource

class TripDetailViewModel(
    private val getTripDetailUseCase: GetTripDetailUseCase,
    private val tripId: Int,
    private val getTripChecklistUseCase: GetTripChecklistUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TripDetailUiState())
    val uiState: StateFlow<TripDetailUiState> = _uiState.asStateFlow()

    init {
        loadTripDetail()
    }

    private fun loadTripDetail() {
        viewModelScope.launch {
            getTripDetailUseCase(tripId)
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _uiState.update { it.copy(isLoading = true, error = null) }
                        }
                        is Resource.Success -> {
                            _uiState.update { it.copy(
                                viaje = result.data,
                                isLoading = false,
                                error = null
                            )}
                        }
                        is Resource.Error -> {
                            _uiState.update { it.copy(
                                isLoading = false,
                                error = result.message
                            )}
                        }
                    }
                }
            checkChecklistStatus()
        }
    }

    private suspend fun checkChecklistStatus() {
        getTripChecklistUseCase(tripId, ChecklistType.SALIDA).collect { result ->
            if (result is Resource.Success) {
                // Un item está completado si tiene vehiculoChecklistDocumentId
                val hasStarted = result.data.items.any { it.vehiculoChecklistDocumentId != null }
                _uiState.update { it.copy(hasDepartureStarted = hasStarted) }
            }
        }

        getTripChecklistUseCase(tripId, ChecklistType.LLEGADA).collect { result ->
            if (result is Resource.Success) {
                val hasStarted = result.data.items.any { it.vehiculoChecklistDocumentId != null }
                _uiState.update { it.copy(hasArrivalStarted = hasStarted) }
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            getTripChecklistUseCase(tripId, ChecklistType.SALIDA).collect { result ->
                if (result is Resource.Success) {
                    val hasStarted = result.data.items.any { it.vehiculoChecklistDocumentId != null }
                    _uiState.update { it.copy(hasDepartureStarted = hasStarted) }
                }
            }

            getTripChecklistUseCase(tripId, ChecklistType.LLEGADA).collect { result ->
                if (result is Resource.Success) {
                    val hasStarted = result.data.items.any { it.vehiculoChecklistDocumentId != null }
                    _uiState.update { it.copy(hasArrivalStarted = hasStarted) }
                }
            }
        }
    }
}