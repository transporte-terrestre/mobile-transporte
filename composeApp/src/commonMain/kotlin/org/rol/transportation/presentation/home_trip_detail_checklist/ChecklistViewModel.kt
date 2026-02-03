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
    private val getAllChecklistItemsUseCase: GetAllChecklistItemsUseCase,
    private val getTripChecklistUseCase: GetTripChecklistUseCase,
    private val upsertTripChecklistUseCase: UpsertTripChecklistUseCase,
    private val tripId: Int,
    tipo: String
) : ViewModel() {

    private val checklistType = ChecklistType.fromString(tipo)

    private var initialState: Map<Int, Boolean> = emptyMap()
    private var initialObservaciones: String = ""

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
                        val itemsBySections = checklist.items.groupBy { it.seccion }
                        val selectedItems = checklist.items.associate { item ->
                            item.checklistItemId to item.completado
                        }

                        initialState = selectedItems
                        initialObservaciones = checklist.observaciones ?: ""

                        _uiState.update {
                            it.copy(
                                checklistItems = checklist.items,
                                itemsBySections = itemsBySections,
                                selectedItems = selectedItems,
                                observaciones = checklist.observaciones ?: "",
                                existingChecklist = checklist,
                                isLoading = false,
                                error = null,
                                hasChanges = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        loadAllItems()
                    }
                }
            }
        }
    }

    private fun loadAllItems() {
        viewModelScope.launch {
            getAllChecklistItemsUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        val items = result.data.map { item ->
                            ChecklistItemDetail(
                                checklistItemId = item.id,
                                nombre = item.nombre,
                                descripcion = item.descripcion,
                                completado = false,
                                seccion = item.seccion,
                                orden = item.orden
                            )
                        }

                        val itemsBySections = items.groupBy { it.seccion }
                        val selectedItems = items.associate { it.checklistItemId to false }

                        initialState = selectedItems
                        initialObservaciones = ""

                        _uiState.update {
                            it.copy(
                                checklistItems = items,
                                itemsBySections = itemsBySections,
                                selectedItems = selectedItems,
                                isLoading = false,
                                error = null,
                                hasChanges = false
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

    fun toggleSection(sectionName: String) {
        val itemsInSection = _uiState.value.itemsBySections[sectionName] ?: return
        val currentSelected = _uiState.value.selectedItems

        val areAllChecked = itemsInSection.all { currentSelected[it.checklistItemId] == true }

        val newState = !areAllChecked

        val newSelectedItems = currentSelected.toMutableMap()
        itemsInSection.forEach { item ->
            newSelectedItems[item.checklistItemId] = newState
        }

        val hasChanges = hasStateChanged(newSelectedItems, _uiState.value.observaciones)

        _uiState.update {
            it.copy(
                selectedItems = newSelectedItems,
                hasChanges = hasChanges
            )
        }
    }

    fun toggleItem(itemId: Int) {
        val currentState = _uiState.value.selectedItems[itemId] ?: false
        val newSelectedItems = _uiState.value.selectedItems.toMutableMap().apply {
            put(itemId, !currentState)
        }


        val hasChanges = hasStateChanged(newSelectedItems, _uiState.value.observaciones)

        _uiState.update {
            it.copy(
                selectedItems = newSelectedItems,
                hasChanges = hasChanges
            )
        }
    }

    fun updateObservations(observations: String) {

        val hasChanges = hasStateChanged(_uiState.value.selectedItems, observations)

        _uiState.update {
            it.copy(
                observaciones = observations,
                hasChanges = hasChanges
            )
        }
    }


    private fun hasStateChanged(
        currentItems: Map<Int, Boolean>,
        currentObservaciones: String
    ): Boolean {
        return currentItems != initialState ||
                currentObservaciones != initialObservaciones
    }


    fun saveChecklist() {
        viewModelScope.launch {
            val state = _uiState.value
            val items = state.selectedItems.toList()

            upsertTripChecklistUseCase(
                tripId = tripId,
                tipo = checklistType,
                items = items,
                observaciones = state.observaciones.ifBlank { null }
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isSaving = true, error = null) }
                    }
                    is Resource.Success -> {
                        val checklistSaved = result.data

                        initialState = state.selectedItems
                        initialObservaciones = state.observaciones

                        _uiState.update {
                            it.copy(
                                isSaving = false,
                                isSaved = true,
                                hasChanges = false,
                                error = null,
                                successMessage = checklistSaved.message ?: "Guardado exitoso"
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isSaving = false,
                                error = result.message
                            )
                        }
                    }
                }
            }
        }
    }
}