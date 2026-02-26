package org.rol.transportation.presentation.home_trip_detail_services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.rol.transportation.data.remote.dto.trip_services.RegistrarServicioRequest
import org.rol.transportation.domain.usecase.GetProximoTramoUseCase
import org.rol.transportation.domain.usecase.GetSegmentsUseCase
import org.rol.transportation.domain.usecase.RegistrarServicioUseCase
import org.rol.transportation.utils.Resource
import org.rol.transportation.domain.usecase.GetLocationUseCase
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.coroutines.flow.catch
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import kotlinx.coroutines.Job
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.LocalDateTime

class TripServicesViewModel(
    private val tripId: Int,
    private val getSegmentsUseCase: GetSegmentsUseCase,
    private val getProximoTramoUseCase: GetProximoTramoUseCase,
    private val registrarServicioUseCase: RegistrarServicioUseCase,
    private val getLocationUseCase: GetLocationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TripServicesUiState())
    val uiState: StateFlow<TripServicesUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private var locationJob: Job? = null

    // ── Permisos y GPS ──

    fun requestPermissionAndStartLocation(permissionsController: PermissionsController) {
        viewModelScope.launch {
            try {
                permissionsController.providePermission(Permission.LOCATION)
                _uiState.update { it.copy(permissionDenied = false) }
                // No iniciamos la ubicación aquí automáticamente.
            } catch (e: DeniedAlwaysException) {
                _uiState.update { it.copy(permissionDenied = true, isLocationLoading = false) }
            } catch (e: DeniedException) {
                _uiState.update { it.copy(permissionDenied = true, isLocationLoading = false) }
            }
        }
    }

    private fun startLocationUpdates() {
        if (locationJob?.isActive == true) return
        locationJob = viewModelScope.launch {
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

    private fun stopLocationUpdates() {
        locationJob?.cancel()
        locationJob = null
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

    // ── Carga de datos ──

    fun loadData() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            getSegmentsUseCase(tripId).collect { result ->
                when (result) {
                    is Resource.Success -> _uiState.update { it.copy(segments = result.data, isLoading = false) }
                    is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                    is Resource.Loading -> {}
                }
            }
        }
    }

    // ── Menú de agregar ──

    fun toggleAddMenu() {
        if (!_uiState.value.showAddMenu) {
            _uiState.update { it.copy(showAddMenu = true, isLoadingMenuProximo = true) }
            viewModelScope.launch {
                getProximoTramoUseCase(tripId).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _uiState.update { it.copy(proximoTramoData = result.data, isLoadingMenuProximo = false) }
                        }
                        is Resource.Error -> {
                            _uiState.update { it.copy(error = result.message, isLoadingMenuProximo = false) }
                        }
                        is Resource.Loading -> {
                            _uiState.update { it.copy(isLoadingMenuProximo = true) }
                        }
                    }
                }
            }
        } else {
            _uiState.update { it.copy(showAddMenu = false) }
        }
    }

    fun dismissAddMenu() {
        _uiState.update { it.copy(showAddMenu = false) }
    }

    fun selectAddOption(option: AddOption) {
        _uiState.update { it.copy(showAddMenu = false, selectedAddOption = option) }

        when (option) {
            AddOption.PROXIMO -> loadProximoTramoAndShowSheet()
            AddOption.OCASIONAL -> showSheetForOcasional()
            AddOption.DESCANSO -> showSheetForDescanso()
        }
    }

    // ── Próximo: carga API proximo-tramo ──

    private fun loadProximoTramoAndShowSheet() {
        _uiState.update { it.copy(isLoadingProximoTramo = true, showLocationSheet = true) }
        viewModelScope.launch {
            getProximoTramoUseCase(tripId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(proximoTramoData = result.data, isLoadingProximoTramo = false)
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(error = result.message, isLoadingProximoTramo = false, showLocationSheet = false)
                        }
                    }
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoadingProximoTramo = true) }
                    }
                }
            }
        }
    }

    // ── Ocasional: solo abre el sheet (usa GPS) ──

    private fun showSheetForOcasional() {
        if (getLocationUseCase.isLocationEnabled()) {
            startLocationUpdates()
        } else {
            _uiState.update { it.copy(gpsDisabled = true, showGpsDialog = true) }
        }

        _uiState.update {
            it.copy(
                showLocationSheet = true,
                isLoadingProximoTramo = true
            )
        }
        // Cargar proximo-tramo para obtener datos previos (ultimoKm, ultimaHora, ultimosPasajeros)
        viewModelScope.launch {
            getProximoTramoUseCase(tripId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _uiState.update { it.copy(proximoTramoData = result.data, isLoadingProximoTramo = false) }
                    }
                    is Resource.Error -> {
                        // Aun así mostrar el sheet, solo que sin datos previos
                        _uiState.update { it.copy(isLoadingProximoTramo = false) }
                    }
                    is Resource.Loading -> {}
                }
            }
        }
    }

    // ── Descanso: abre sheet con datos previos ──

    private fun showSheetForDescanso() {
        _uiState.update {
            it.copy(
                showLocationSheet = true,
                isLoadingProximoTramo = true
            )
        }
        viewModelScope.launch {
            getProximoTramoUseCase(tripId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _uiState.update { it.copy(proximoTramoData = result.data, isLoadingProximoTramo = false) }
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(isLoadingProximoTramo = false) }
                    }
                    is Resource.Loading -> {}
                }
            }
        }
    }

    // ── Registrar servicio ──

    /**
     * Registra servicio para "Próximo" (usa tipo del proximo-tramo).
     */
    fun registrarServicio(
        horaActual: String,
        kilometrajeActual: Double,
        cantidadPasajeros: Int
    ) {
        val proximo = _uiState.value.proximoTramoData ?: return
        val location = _uiState.value.currentLocation

        val isoDate = formatTimeWithCurrentDate(horaActual)
        val request = RegistrarServicioRequest(
            longitud = location?.longitude ?: 0.0,
            latitud = location?.latitude ?: 0.0,
            horaActual = isoDate,
            nombreLugar = proximo.nombreLugar ?: "",
            kilometrajeActual = kilometrajeActual,
            cantidadPasajeros = cantidadPasajeros,
            rutaParadaId = proximo.rutaParadaId
        )

        submitRegistro(proximo.tipo, request)
    }

    /**
     * Registra parada ocasional (tipo fijo = "parada").
     */
    fun registrarParadaOcasional(
        nombreLugar: String,
        horaActual: String,
        kilometrajeActual: Double,
        cantidadPasajeros: Int
    ) {
        val location = _uiState.value.currentLocation

        val isoDate = formatTimeWithCurrentDate(horaActual)
        val request = RegistrarServicioRequest(
            longitud = location?.longitude ?: 0.0,
            latitud = location?.latitude ?: 0.0,
            horaActual = isoDate,
            nombreLugar = nombreLugar,
            kilometrajeActual = kilometrajeActual,
            cantidadPasajeros = cantidadPasajeros,
            rutaParadaId = null
        )

        submitRegistro("parada", request)
    }

    /**
     * Registra descanso (tipo fijo = "descanso").
     */
    fun registrarDescanso(
        horaActual: String,
        kilometrajeActual: Double,
        cantidadPasajeros: Int
    ) {
        val location = _uiState.value.currentLocation

        val isoDate = formatTimeWithCurrentDate(horaActual)
        val request = RegistrarServicioRequest(
            longitud = location?.longitude ?: 0.0,
            latitud = location?.latitude ?: 0.0,
            horaActual = isoDate,
            nombreLugar = "Descanso",
            kilometrajeActual = kilometrajeActual,
            cantidadPasajeros = cantidadPasajeros,
            rutaParadaId = null
        )

        submitRegistro("descanso", request)
    }

    private fun submitRegistro(tipo: String, request: RegistrarServicioRequest) {
        viewModelScope.launch {
            registrarServicioUseCase(tripId, tipo, request).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { it.copy(isCreating = true) }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isCreating = false,
                                showLocationSheet = false,
                                proximoTramoData = null,
                                selectedAddOption = null,
                                createSuccess = "Servicio registrado correctamente"
                            )
                        }
                        stopLocationUpdates()
                        loadData()
                    }
                    is Resource.Error -> _uiState.update { it.copy(isCreating = false, error = result.message) }
                }
            }
        }
    }

    // ── Utilidades ──

    fun clearMessages() {
        _uiState.update { it.copy(error = null, createSuccess = null) }
    }

    fun hideLocationSheet() {
        stopLocationUpdates()
        _uiState.update { it.copy(showLocationSheet = false, selectedAddOption = null, proximoTramoData = null) }
    }

    private fun formatTimeWithCurrentDate(timeStr: String): String {
        return try {
            val parts = timeStr.split(":")
            if (parts.size >= 2) {
                val hour = parts[0].toIntOrNull() ?: 0
                val min = parts[1].toIntOrNull() ?: 0
                val tz = TimeZone.currentSystemDefault()
                val now = Clock.System.now().toLocalDateTime(tz)
                val m = now.monthNumber.toString().padStart(2, '0')
                val d = now.dayOfMonth.toString().padStart(2, '0')
                val h = hour.toString().padStart(2, '0')
                val minStr = min.toString().padStart(2, '0')
                "${now.year}-$m-${d}T$h:$minStr:00.000Z"
            } else {
                Clock.System.now().toString()
            }
        } catch (e: Exception) {
            Clock.System.now().toString()
        }
    }
}