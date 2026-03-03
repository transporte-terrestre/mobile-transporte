package org.rol.transportation.presentation.home_trip_detail_services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.rol.transportation.domain.usecase.DeleteSegmentUseCase
import org.rol.transportation.domain.usecase.GetLocationUseCase
import org.rol.transportation.domain.usecase.GetNextStepUseCase
import org.rol.transportation.domain.usecase.GetSegmentsUseCase
import org.rol.transportation.domain.usecase.UpdateSegmentUseCase
import org.rol.transportation.utils.Resource

class TripServicesViewModel(
    private val tripId: Int,
    private val getSegmentsUseCase: GetSegmentsUseCase,
    private val getNextStepUseCase: GetNextStepUseCase,
    private val getLocationUseCase: GetLocationUseCase,
    private val deleteSegmentUseCase: DeleteSegmentUseCase,
    private val updateSegmentUseCase: UpdateSegmentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TripServicesUiState())
    val uiState: StateFlow<TripServicesUiState> = _uiState.asStateFlow()

    fun requestPermissionAndStartLocation(permissionsController: PermissionsController) {
        viewModelScope.launch {
            try {
                permissionsController.providePermission(Permission.LOCATION)
                _uiState.update { it.copy(permissionDenied = false) }
                if (getLocationUseCase.isLocationEnabled()) {
                    startLocationUpdates()
                } else {
                    _uiState.update { it.copy(gpsDisabled = true, showGpsDialog = true) }
                }
            } catch (e: DeniedAlwaysException) {
                _uiState.update { it.copy(permissionDenied = true, isLocationLoading = false) }
            } catch (e: DeniedException) {
                _uiState.update { it.copy(permissionDenied = true, isLocationLoading = false) }
            }
        }
    }

    private fun startLocationUpdates() {
        println("GPS_DEBUG: startLocationUpdates() en el ViewModel disparado")
        viewModelScope.launch {
            _uiState.update { it.copy(isLocationLoading = true, gpsDisabled = false, showGpsDialog = false) }
            getLocationUseCase()
                .catch { e ->
                    println("GPS_DEBUG: Exception capturada: ${e.message}")
                    _uiState.update { it.copy(error = e.message, isLocationLoading = false) }
                }
                .collect { location ->
                    println("GPS_DEBUG: recolectado nuevo location -> lat: ${location.latitude}, lng: ${location.longitude}")
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

    fun loadData() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            getSegmentsUseCase(tripId).collect { result ->
                if (result is Resource.Success) {
                    // Ordenamos por ID para mantener el orden cronológico de registro
                    val segments = (result.data ?: emptyList()).sortedBy { it.id }
                    val hasDestino = segments.any { it.tipo.lowercase() == "destino" }
                    _uiState.update { it.copy(
                        segments = segments,
                        isAddButtonVisible = !hasDestino
                    ) }
                }
            }
        }

        viewModelScope.launch {

            getNextStepUseCase(tripId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val data = result.data

                        _uiState.update {
                            it.copy(
                                nextStepData = data,
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isAddButtonVisible = false,
                                nextStepData = null,
                                isLoading = false
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun openCreateDialog() {
        if (_uiState.value.nextStepData != null) {
            _uiState.update { it.copy(showDialog = true, error = null) }
        }
    }

    fun closeDialog() {
        _uiState.update { it.copy(showDialog = false) }
    }

    // Se delegará a las nuevas pantallas el registro, por lo que creamos
    // un flujo externo para capturar "createSuccess" usando notifySuccess()
    fun notifySuccess(message: String) {
        _uiState.update { it.copy(createSuccess = message, showDialog = false) }
        loadData()
    }

    fun clearMessages() {
        _uiState.update { it.copy(error = null, createSuccess = null) }
    }

    fun promptDeleteLastSegment() {
        _uiState.update { it.copy(isDeletingSegment = true) }
    }

    fun dismissDeletePrompt() {
        _uiState.update { it.copy(isDeletingSegment = false) }
    }

    fun deleteLastSegment() {
        val lastSegment = _uiState.value.segments.lastOrNull() ?: return
        _uiState.update { it.copy(isLoading = true, isDeletingSegment = false) }
        viewModelScope.launch {
            deleteSegmentUseCase(lastSegment.id).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _uiState.update { it.copy(isLoading = false, createSuccess = "Tramo eliminado correctamente") }
                        loadData()
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(isLoading = false, error = result.message) }
                    }
                    else -> {}
                }
            }
        }
    }

    fun openEditDialog(segment: org.rol.transportation.data.remote.dto.trip_services.SegmentDto) {
        _uiState.update { it.copy(editingSegment = segment) }
    }

    fun closeEditDialog() {
        _uiState.update { it.copy(editingSegment = null) }
    }

    fun updateSegment(request: org.rol.transportation.data.remote.dto.trip_services.UpdateSegmentRequest) {
        val segment = _uiState.value.editingSegment ?: return
        _uiState.update { it.copy(isLoading = true, editingSegment = null) }
        viewModelScope.launch {
            updateSegmentUseCase(segment.id, request).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _uiState.update { it.copy(isLoading = false, createSuccess = "Tramo actualizado correctamente") }
                        loadData()
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(isLoading = false, error = result.message) }
                    }
                    else -> {}
                }
            }
        }
    }
}
