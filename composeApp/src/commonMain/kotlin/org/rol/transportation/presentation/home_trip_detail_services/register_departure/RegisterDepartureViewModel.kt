package org.rol.transportation.presentation.home_trip_detail_services.register_departure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.rol.transportation.utils.AppEventBus
import org.rol.transportation.data.remote.dto.trip_services.RegisterLocationRequest
import org.rol.transportation.domain.model.LocationModel
import org.rol.transportation.domain.usecase.GetNextStepUseCase
import org.rol.transportation.domain.usecase.RegisterDepartureUseCase
import org.rol.transportation.utils.Resource

class RegisterDepartureViewModel(
    private val tripId: Int,
    private val getNextStepUseCase: GetNextStepUseCase,
    private val registerDepartureUseCase: RegisterDepartureUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterDepartureUiState())
    val uiState: StateFlow<RegisterDepartureUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        if (_uiState.value.nextStepData == null) {
            _uiState.update { it.copy(isLoading = true, error = null) }
        }
        viewModelScope.launch {
            getNextStepUseCase(tripId, "origen").collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val data = result.data
                        val location = if (data.latitud != null && data.longitud != null) {
                            LocationModel(latitude = data.latitud.toDouble(), longitude = data.longitud.toDouble())
                        } else null
                        
                        _uiState.update { it.copy(
                            nextStepData = data, 
                            currentLocation = location,
                            isLoading = false
                        ) }
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(error = result.message, isLoading = false) }
                    }
                    else -> {}
                }
            }
        }
    }



    fun clearMessages() {
        _uiState.update { it.copy(error = null, successMessage = null) }
    }

    fun registerDeparture(
        horaActual: String,
        kilometrajeActual: Double,
        cantidadPasajeros: Int,
        nombreLugar: String
    ) {
        val nextStep = _uiState.value.nextStepData ?: return
        val currentLocation = _uiState.value.currentLocation ?: return

        val request = RegisterLocationRequest(
            longitud = currentLocation.longitude,
            latitud = currentLocation.latitude,
            cantidadPasajeros = cantidadPasajeros,
            horaActual = horaActual,
            kilometrajeActual = kilometrajeActual,
            nombreLugar = nombreLugar,
            rutaParadaId = nextStep.rutaParadaId
        )

        _uiState.update { it.copy(isRegistering = true, successMessage = "Iniciando viaje...", error = null) }

        GlobalScope.launch {
            registerDepartureUseCase(tripId, request).collect { result ->
                when(result) {
                    is Resource.Success -> AppEventBus.triggerReload()
                    else -> {}
                }
            }
        }
    }
}
