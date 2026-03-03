package org.rol.transportation.presentation.home_trip_detail_passenger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.rol.transportation.domain.usecase.GetPassengersUseCase
import org.rol.transportation.utils.Resource

class PassengerViewModel(
    private val tripId: Int,
    private val viajeTramoId: Int?,
    private val getPassengersUseCase: GetPassengersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PassengerUiState())
    val uiState: StateFlow<PassengerUiState> = _uiState.asStateFlow()

    init {
        loadPassengers()
    }

    fun loadPassengers() {
        viewModelScope.launch {
            getPassengersUseCase(tripId, viajeTramoId).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true, error = null) }
                    is Resource.Success -> _uiState.update {
                        it.copy(isLoading = false, passengers = result.data)
                    }
                    is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }



    fun clearMessages() {
        _uiState.update { it.copy(error = null, successMessage = null) }
    }
}