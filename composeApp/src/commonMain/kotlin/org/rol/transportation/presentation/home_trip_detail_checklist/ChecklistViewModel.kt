package org.rol.transportation.presentation.home_trip_detail_checklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.rol.transportation.domain.model.ChecklistItemDetail
import org.rol.transportation.domain.model.enums.ChecklistType
import org.rol.transportation.domain.usecase.GetAllChecklistItemsUseCase
import org.rol.transportation.domain.usecase.GetTripChecklistUseCase
import org.rol.transportation.domain.usecase.UpsertTripChecklistUseCase
import org.rol.transportation.utils.Resource

class ChecklistViewModel(
    private val getTripChecklistUseCase: GetTripChecklistUseCase,
    private val tripId: Int,
    tipo: String
) : ViewModel() {

    private val checklistType = ChecklistType.fromString(tipo)

    private val _uiState = MutableStateFlow(ChecklistUiState(tipo = checklistType))
    val uiState: StateFlow<ChecklistUiState> = _uiState.asStateFlow()

    init {
        loadChecklist()
    }

    private fun loadChecklist() {
        viewModelScope.launch {
            getTripChecklistUseCase(tripId, checklistType).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        val checklist = result.data

                        _uiState.update {
                            it.copy(
                                checklistItems = checklist.items.sortedBy { item -> item.orden },
                                observaciones = checklist.items.firstOrNull()?.observacion ?: "",
                                existingChecklist = checklist,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun onItemClick(item: ChecklistItemDetail) {
        // Aquí puedes emitir un evento o manejar la navegación
    }

    fun retry() {
        loadChecklist()
    }
}