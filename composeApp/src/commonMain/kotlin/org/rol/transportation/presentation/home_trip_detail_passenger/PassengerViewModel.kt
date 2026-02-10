package org.rol.transportation.presentation.home_trip_detail_passenger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.rol.transportation.domain.usecase.GetPassengersUseCase
import org.rol.transportation.domain.usecase.UpsertPassengersUseCase
import org.rol.transportation.utils.Resource

class PassengerViewModel(
    private val tripId: Int,
    private val getPassengersUseCase: GetPassengersUseCase,
    private val upsertPassengersUseCase: UpsertPassengersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PassengerUiState())
    val uiState: StateFlow<PassengerUiState> = _uiState.asStateFlow()

    init {
        loadPassengers()
    }

    fun loadPassengers() {
        viewModelScope.launch {
            getPassengersUseCase(tripId).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true, error = null) }
                    is Resource.Success -> _uiState.update {
                        it.copy(isLoading = false, passengers = result.data, hasChanges = false)
                    }
                    is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }

    fun toggleAttendance(pasajeroId: Int, isChecked: Boolean) {
        _uiState.update { currentState ->
            val updatedList = currentState.passengers.map { item ->
                if (item.pasajeroId == pasajeroId) item.copy(asistencia = isChecked) else item
            }
            currentState.copy(passengers = updatedList, hasChanges = true)
        }
    }

    fun save() {
        viewModelScope.launch {
            upsertPassengersUseCase(tripId, _uiState.value.passengers).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { it.copy(isSaving = true, error = null) }
                    is Resource.Success -> _uiState.update {
                        it.copy(isSaving = false, successMessage = "Asistencia guardada correctamente", hasChanges = false, passengers = result.data)
                    }
                    is Resource.Error -> _uiState.update { it.copy(isSaving = false, error = result.message) }
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(error = null, successMessage = null) }
    }
}