package org.rol.transportation.presentation.home_trip_detail_services.register_arrival

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.rol.transportation.domain.usecase.GetNextStepUseCase
import kotlinx.datetime.Clock
import org.rol.transportation.data.remote.dto.trip_services.RegisterLocationRequest
import org.rol.transportation.domain.model.LocationModel
import org.rol.transportation.domain.usecase.RegisterArrivalUseCase
import org.rol.transportation.utils.AppEventBus
import org.rol.transportation.utils.Resource

class RegisterArrivalViewModel(
    private val tripId: Int,
    private val getNextStepUseCase: GetNextStepUseCase,
    private val registerArrivalUseCase: RegisterArrivalUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterArrivalUiState())
    val uiState: StateFlow<RegisterArrivalUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        if (_uiState.value.nextStepData == null) {
            _uiState.update { it.copy(isLoading = true, error = null) }
        }
        println("ROLC_TAG: [RegisterArrivalViewModel] Fetching getNextStepUseCase(destino)...")
        val start = kotlin.time.Clock.System.now().toEpochMilliseconds()
        viewModelScope.launch {
            getNextStepUseCase(tripId, "destino").collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val duration = kotlin.time.Clock.System.now().toEpochMilliseconds() - start
                        println("ROLC_TAG: [RegisterArrivalViewModel] getNextStepUseCase demoró ${duration}ms")
                        val data = result.data
                        val location = if (data.latitud != null && data.longitud != null) {
                            LocationModel(
                                latitude = data.latitud.toDouble(),
                                longitude = data.longitud.toDouble()
                            )
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

    fun registerArrival(
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

        _uiState.update { it.copy(isRegistering = true, successMessage = "Terminando viaje...", error = null) }

        GlobalScope.launch {
            registerArrivalUseCase(tripId, request).collect { result ->
                when(result) {
                    is Resource.Success -> AppEventBus.triggerReload()
                    else -> {}
                }
            }
        }
    }
}
