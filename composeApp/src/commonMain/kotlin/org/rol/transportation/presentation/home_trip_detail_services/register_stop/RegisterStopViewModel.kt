package org.rol.transportation.presentation.home_trip_detail_services.register_stop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.rol.transportation.utils.AppEventBus
import org.rol.transportation.data.remote.dto.trip_services.RegisterLocationRequest
import org.rol.transportation.domain.usecase.GetLocationUseCase
import org.rol.transportation.domain.usecase.GetNextStepUseCase
import org.rol.transportation.domain.usecase.RegisterStopUseCase
import kotlinx.datetime.Clock
import org.rol.transportation.utils.Resource

class RegisterStopViewModel(
    private val tripId: Int,
    private val getNextStepUseCase: GetNextStepUseCase,
    private val registerStopUseCase: RegisterStopUseCase,
    private val getLocationUseCase: GetLocationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterStopUiState())
    val uiState: StateFlow<RegisterStopUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        if (_uiState.value.nextStepData == null) {
            _uiState.update { it.copy(isLoading = true, error = null) }
        }
        println("ROLC_TAG: [RegisterStopViewModel] Fetching getNextStepUseCase(parada)...")
        val start = kotlin.time.Clock.System.now().toEpochMilliseconds()
        viewModelScope.launch {
            getNextStepUseCase(tripId, "parada").collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val duration = kotlin.time.Clock.System.now().toEpochMilliseconds() - start
                        println("ROLC_TAG: [RegisterStopViewModel] getNextStepUseCase(parada) demoró ${duration}ms")
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
        println("ROLC_TAG: [RegisterStopViewModel] startLocationUpdates()...")
        val startLoc = kotlin.time.Clock.System.now().toEpochMilliseconds()
        
        // Optimización brutal: Si ya tenemos currentLocation pre-cargada y cacheada antes (p. ej. en la pantalla padre de Tramos), no esperamos 20 segundos a que el GPS en frio caliente para darnos la ubicación, simplemente tomamos la ultima.
        viewModelScope.launch {
            _uiState.update { it.copy(isLocationLoading = true, gpsDisabled = false, showGpsDialog = false) }

            // Usamos getLastKnown() instantaneo
            val lastLocation = getLocationUseCase.getLastKnown()
            if (lastLocation != null) {
                val dur = kotlin.time.Clock.System.now().toEpochMilliseconds() - startLoc
                println("ROLC_TAG: [RegisterStopViewModel] Se usó getLastKnown Location en ${dur}ms fast-track!")
                _uiState.update { it.copy(currentLocation = lastLocation, isLocationLoading = false) }
            }

            // A la par, nos conectamos al flujo continuo para estar finos, si se demoran 40 seg ya no nos congela la pantalla
            getLocationUseCase()
                .catch { e ->
                    println("ROLC_TAG: [RegisterStopViewModel] startLocationUpdates error")
                    _uiState.update { it.copy(error = e.message, isLocationLoading = false) }
                }
                .collect { location ->
                    println("ROLC_TAG: [RegisterStopViewModel] Update fino del GPS")
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

    fun registerStop(
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
            nombreLugar = nombreLugar,
            rutaParadaId = _uiState.value.nextStepData?.rutaParadaId
        )

        _uiState.update { it.copy(isRegistering = true, successMessage = "Guardando parada...", error = null) }

        GlobalScope.launch {
            registerStopUseCase(tripId, request).collect { result ->
                when(result) {
                    is Resource.Success -> AppEventBus.triggerReload()
                    else -> {}
                }
            }
        }
    }
}
