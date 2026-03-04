package org.rol.transportation.presentation.home_trip_detail_services.scan_passenger_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.rol.transportation.domain.usecase.GetPassengersUseCase
import org.rol.transportation.utils.AppEventBus
import org.rol.transportation.utils.Resource

class ScanPassengerListViewModel(
    private val tripId: Int,
    private val viajeTramoId: Int,
    private val getPassengersUseCase: GetPassengersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScanPassengerListUiState())
    val uiState: StateFlow<ScanPassengerListUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadPassengers()
        viewModelScope.launch {
            AppEventBus.reloadTripServices.collect {
                loadPassengers()
            }
        }
    }

    fun loadPassengers() {
        if (_uiState.value.passengers.isEmpty()) {
            _uiState.update { it.copy(isLoading = true, error = null) }
        }
        
        loadJob?.cancel()
        println("ROLC_TAG: [ScanPassengerListViewModel] Fetching getPassengersUseCase...")
        loadJob = viewModelScope.launch {
            val start = kotlin.time.Clock.System.now().toEpochMilliseconds()
            getPassengersUseCase(tripId, viajeTramoId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        if (_uiState.value.passengers.isEmpty()) {
                            _uiState.update { it.copy(isLoading = true, error = null) }
                        }
                    }
                    is Resource.Success -> {
                        val duration = kotlin.time.Clock.System.now().toEpochMilliseconds() - start
                        println("ROLC_TAG: [ScanPassengerListViewModel] getPassengersUseCase demoró ${duration}ms")
                        _uiState.update {
                            it.copy(isLoading = false, passengers = result.data ?: emptyList())
                        }
                    }
                    is Resource.Error -> {
                        println("ROLC_TAG: [ScanPassengerListViewModel] getPassengersUseCase error")
                        _uiState.update { it.copy(isLoading = false, error = result.message) }
                    }
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(error = null, successMessage = null) }
    }
}
