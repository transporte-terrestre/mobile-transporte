package org.rol.transportation.presentation.home_trip_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.rol.transportation.domain.model.enums.ChecklistType
import org.rol.transportation.domain.usecase.GetTripChecklistUseCase
import org.rol.transportation.domain.usecase.GetTripDetailUseCase
import org.rol.transportation.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


class TripDetailViewModel(
    private val getTripDetailUseCase: GetTripDetailUseCase,
    private val tripId: Int,
    private val getTripChecklistUseCase: GetTripChecklistUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TripDetailUiState())
    val uiState: StateFlow<TripDetailUiState> = _uiState.asStateFlow()

    private var detailJob: Job? = null
    private var salidaJob: Job? = null
    private var llegadaJob: Job? = null

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        val start = kotlin.time.Clock.System.now().toEpochMilliseconds()
        println("ROLC_TAG: [TripDetailViewModel] Empezando a cargar getTripDetailUseCase...")
        
        detailJob?.cancel()
        detailJob = viewModelScope.launch {
            getTripDetailUseCase(tripId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val duration = kotlin.time.Clock.System.now().toEpochMilliseconds() - start
                        println("ROLC_TAG: [TripDetailViewModel] getTripDetailUseCase terminó en ${duration}ms")
                        _uiState.update { it.copy(viaje = result.data, isLoading = false) }
                    }
                    is Resource.Error -> {
                        println("ROLC_TAG: [TripDetailViewModel] getTripDetailUseCase dio ERROR")
                        _uiState.update { it.copy(isLoading = false, error = result.message) }
                    }
                    else -> {}
                }
            }
        }
        fetchChecklistStatuses()
    }

    fun refreshData() {
        fetchChecklistStatuses()
    }

    private fun fetchChecklistStatuses() {
        println("ROLC_TAG: [TripDetailViewModel] Iniciando fetchChecklistStatuses SALIDA y LLEGADA en paralelo")
        val startChecklist = kotlin.time.Clock.System.now().toEpochMilliseconds()
        
        // Status Salida
        salidaJob?.cancel()
        salidaJob = viewModelScope.launch {
            getTripChecklistUseCase(tripId, ChecklistType.SALIDA).collect { result ->
                if (result is Resource.Success) {
                    val currentDuration = kotlin.time.Clock.System.now().toEpochMilliseconds() - startChecklist
                    println("ROLC_TAG: [TripDetailViewModel] CheckList SALIDA terminó en ${currentDuration}ms")
                    val hasStarted = result.data.items.any { it.vehiculoChecklistDocumentId != null }
                    _uiState.update { it.copy(hasDepartureStarted = hasStarted) }
                }
            }
        }

        // Status Llegada
        llegadaJob?.cancel()
        llegadaJob = viewModelScope.launch {
            getTripChecklistUseCase(tripId, ChecklistType.LLEGADA).collect { result ->
                if (result is Resource.Success) {
                    val currentDuration = kotlin.time.Clock.System.now().toEpochMilliseconds() - startChecklist
                    println("ROLC_TAG: [TripDetailViewModel] CheckList LLEGADA terminó en ${currentDuration}ms")
                    val hasStarted = result.data.items.any { it.vehiculoChecklistDocumentId != null }
                    _uiState.update { it.copy(hasArrivalStarted = hasStarted) }
                }
            }
        }
    }
}