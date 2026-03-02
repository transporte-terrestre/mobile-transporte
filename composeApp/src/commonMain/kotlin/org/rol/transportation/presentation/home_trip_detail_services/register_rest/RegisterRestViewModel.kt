package org.rol.transportation.presentation.home_trip_detail_services.register_rest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.rol.transportation.data.remote.dto.trip_services.RegisterLocationRequest
import org.rol.transportation.domain.usecase.GetLocationUseCase
import org.rol.transportation.domain.usecase.GetNextStepUseCase
import org.rol.transportation.domain.usecase.RegisterRestUseCase
import org.rol.transportation.utils.Resource

class RegisterRestViewModel(
    private val tripId: Int,
    private val getNextStepUseCase: GetNextStepUseCase,
    private val registerRestUseCase: RegisterRestUseCase,
    private val getLocationUseCase: GetLocationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterRestUiState())
    val uiState: StateFlow<RegisterRestUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            getNextStepUseCase(tripId, "descanso").collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _uiState.update { it.copy(nextStepData = result.data, isLoading = false) }
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(error = result.message, isLoading = false) }
                    }
                    else -> {}
                }
            }
        }
    }

    fun startLocation() {
        if (getLocationUseCase.isLocationEnabled()) {
            startLocationUpdates()
        } else {
            _uiState.update { it.copy(gpsDisabled = true, showGpsDialog = true) }
        }
    }

    private fun startLocationUpdates() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLocationLoading = true, gpsDisabled = false, showGpsDialog = false) }
            getLocationUseCase()
                .catch { e ->
                    _uiState.update { it.copy(error = e.message, isLocationLoading = false) }
                }
                .collect { location ->
                    _uiState.update { it.copy(currentLocation = location, isLocationLoading = false) }
                }
        }
    }

    fun openLocationSettings() {
        getLocationUseCase.openLocationSettings()
        _uiState.update { it.copy(showGpsDialog = false) }
    }

    fun dismissGpsDialog() {
        _uiState.update { it.copy(showGpsDialog = false) }
    }

    fun retryLocationAfterSettings() {
        if (getLocationUseCase.isLocationEnabled()) {
            startLocationUpdates()
        } else {
            _uiState.update { it.copy(gpsDisabled = true, showGpsDialog = true) }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(error = null, successMessage = null) }
    }

    fun registerRest(
        horaActual: String,
        kilometrajeActual: Double,
        cantidadPasajeros: Int,
        nombreLugar: String
    ) {
        val currentLocation = _uiState.value.currentLocation ?: return

        val request = RegisterLocationRequest(
            longitud = currentLocation.longitude,
            latitud = currentLocation.latitude,
            cantidadPasajeros = cantidadPasajeros,
            horaActual = horaActual,
            kilometrajeActual = kilometrajeActual,
            nombreLugar = nombreLugar
        )

        viewModelScope.launch {
            registerRestUseCase(tripId, request).collect { result ->
                when(result) {
                    is Resource.Loading -> _uiState.update { it.copy(isRegistering = true, error = null) }
                    is Resource.Success -> {
                        _uiState.update { it.copy(isRegistering = false, successMessage = "Descanso registrado correctamente") }
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(isRegistering = false, error = result.message) }
                    }
                }
            }
        }
    }
}
