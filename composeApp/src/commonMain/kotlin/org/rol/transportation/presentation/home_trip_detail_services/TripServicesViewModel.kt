package org.rol.transportation.presentation.home_trip_detail_services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.rol.transportation.data.remote.dto.trip_services.CreateSegmentRequest
import org.rol.transportation.domain.usecase.CreateSegmentUseCase
import org.rol.transportation.domain.usecase.GetNextStepUseCase
import org.rol.transportation.domain.usecase.GetSegmentsUseCase
import org.rol.transportation.utils.Resource

class TripServicesViewModel(
    private val tripId: Int,
    private val getSegmentsUseCase: GetSegmentsUseCase,
    private val getNextStepUseCase: GetNextStepUseCase,
    private val createSegmentUseCase: CreateSegmentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TripServicesUiState())
    val uiState: StateFlow<TripServicesUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            getSegmentsUseCase(tripId).collect { result ->
                if (result is Resource.Success) {
                    _uiState.update { it.copy(segments = result.data) }
                }
            }

            getNextStepUseCase(tripId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val data = result.data

                        val hasValidId = data.paradaPartidaId != null

                        // Parsear "2/1", "1/1", "3/2", etc.
                        val isOverflow = try {
                            val parts = data.progreso?.split("/")
                            if (parts?.size == 2) {
                                val current = parts[0].toInt()
                                val total = parts[1].toInt()
                                // Si el paso actual (2) es mayor al total (1), es un desbordamiento.
                                current > total
                            } else {
                                false
                            }
                        } catch (e: Exception) {
                            false
                        }

                        // El botón solo se ve si hay ID válido Y NO nos hemos pasado del total
                        val shouldShowButton = hasValidId && !isOverflow

                        _uiState.update {
                            it.copy(
                                isAddButtonVisible = shouldShowButton,
                                nextStepData = if (shouldShowButton) data else null,
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

    fun createSegment(
        paradaPartidaId: Int,
        paradaLlegadaId: Int,
        horaTermino: String,
        kmFinal: Double,
        numeroPasajeros: Int,
        observaciones: String
    ) {
        val nextStep = _uiState.value.nextStepData ?: return

        val request = CreateSegmentRequest(
            paradaPartidaId = paradaPartidaId,
            paradaLlegadaId = paradaLlegadaId,
            horaSalida = nextStep.horaSalida ?: "00:00:00",
            horaTermino = horaTermino,
            kmInicial = nextStep.kmInicial ?: 0.0,
            kmFinal = kmFinal,
            numeroPasajeros = numeroPasajeros,
            observaciones = observaciones
        )

        viewModelScope.launch {
            createSegmentUseCase(tripId, request).collect { result ->
                when(result) {
                    is Resource.Loading -> _uiState.update { it.copy(isCreating = true) }
                    is Resource.Success -> {
                        _uiState.update { it.copy(isCreating = false, showDialog = false, createSuccess = "Tramo registrado correctamente") }
                        loadData() // Recargar todo
                    }
                    is Resource.Error -> _uiState.update { it.copy(isCreating = false, error = result.message) }
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(error = null, createSuccess = null) }
    }
}