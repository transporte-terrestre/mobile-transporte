package org.rol.transportation.presentation.checklist_inspection_sheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.inspection_sheet.InspectionItem
import org.rol.transportation.domain.usecase.GetInspectionSheetUseCase
import org.rol.transportation.domain.usecase.UpsertInspectionSheetUseCase
import org.rol.transportation.utils.Resource

class InspectionSheetViewModel(
    private val getInspectionSheetUseCase: GetInspectionSheetUseCase,
    private val upsertInspectionSheetUseCase: UpsertInspectionSheetUseCase,
    private val vehiculoId: Int,
    private val viajeId: Int,
    private val tipo: String,
    private val safeDocumentId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        InspectionSheetUiState(
            vehiculoId = vehiculoId,
            viajeId = viajeId,
            viajeTipo = TripType.fromString(tipo) ?: TripType.SALIDA
        )
    )
    val uiState: StateFlow<InspectionSheetUiState> = _uiState.asStateFlow()

    init {
        loadInspectionSheet()
    }


    private fun loadInspectionSheet() {

        val documentId = if (safeDocumentId == -1) null else safeDocumentId

        viewModelScope.launch {
            getInspectionSheetUseCase(vehiculoId, documentId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                hojaInspeccion = result.data,
                                isLoading = false,
                                error = null,
                                hasChanges = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(isLoading = false, error = result.message) }
                    }
                }
            }
        }
    }


    fun onItemValueChanged(sectionKey: String, itemKey: String, newValue: Boolean) {
        val currentSheet = _uiState.value.hojaInspeccion ?: return

        val updatedDocument = when (sectionKey) {
            "declaracionJurada" -> {
                val updatedItems = currentSheet.document.declaracionJurada.items.toMutableMap()
                updatedItems[itemKey] = updatedItems[itemKey]?.copy(value = newValue)
                    ?: InspectionItem("", "", newValue)
                currentSheet.document.copy(
                    declaracionJurada = currentSheet.document.declaracionJurada.copy(items = updatedItems)
                )
            }
            "estadoGeneral" -> {
                val updatedItems = currentSheet.document.estadoGeneral.items.toMutableMap()
                updatedItems[itemKey] = updatedItems[itemKey]?.copy(value = newValue)
                    ?: InspectionItem("", "", newValue)
                currentSheet.document.copy(
                    estadoGeneral = currentSheet.document.estadoGeneral.copy(items = updatedItems)
                )
            }
            "estadoInterno" -> {
                val updatedItems = currentSheet.document.estadoInterno.items.toMutableMap()
                updatedItems[itemKey] = updatedItems[itemKey]?.copy(value = newValue)
                    ?: InspectionItem("", "", newValue)
                currentSheet.document.copy(
                    estadoInterno = currentSheet.document.estadoInterno.copy(items = updatedItems)
                )
            }
            "elementosSeguridad" -> {
                val updatedItems = currentSheet.document.elementosSeguridad.items.toMutableMap()
                updatedItems[itemKey] = updatedItems[itemKey]?.copy(value = newValue)
                    ?: InspectionItem("", "", newValue)
                currentSheet.document.copy(
                    elementosSeguridad = currentSheet.document.elementosSeguridad.copy(items = updatedItems)
                )
            }
            "estadoMecanico" -> {
                val updatedItems = currentSheet.document.estadoMecanico.items.toMutableMap()
                updatedItems[itemKey] = updatedItems[itemKey]?.copy(value = newValue)
                    ?: InspectionItem("", "", newValue)
                currentSheet.document.copy(
                    estadoMecanico = currentSheet.document.estadoMecanico.copy(items = updatedItems)
                )
            }
            "sistemasCriticos" -> {
                val updatedItems = currentSheet.document.sistemasCriticos.items.toMutableMap()
                updatedItems[itemKey] = updatedItems[itemKey]?.copy(value = newValue)
                    ?: InspectionItem("", "", newValue)
                currentSheet.document.copy(
                    sistemasCriticos = currentSheet.document.sistemasCriticos.copy(items = updatedItems)
                )
            }
            "cinturonesSeguridad" -> {
                val updatedItems = currentSheet.document.cinturonesSeguridad.items.toMutableMap()
                updatedItems[itemKey] = updatedItems[itemKey]?.copy(value = newValue)
                    ?: InspectionItem("", "", newValue)
                currentSheet.document.copy(
                    cinturonesSeguridad = currentSheet.document.cinturonesSeguridad.copy(items = updatedItems)
                )
            }
            else -> currentSheet.document
        }

        _uiState.update {
            it.copy(
                hojaInspeccion = currentSheet.copy(document = updatedDocument),
                hasChanges = true
            )
        }
    }

    fun saveInspectionSheet() {
        val inspectionSheet = _uiState.value.hojaInspeccion ?: return
        val typeTrip = _uiState.value.viajeTipo

        viewModelScope.launch {
            upsertInspectionSheetUseCase(
                vehiculoId = vehiculoId,
                viajeId = viajeId,
                viajeTipo = typeTrip,
                hojaInspeccion = inspectionSheet
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isSaving = true, error = null) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                hojaInspeccion = result.data,
                                isSaving = false,
                                hasChanges = false,
                                successMessage = "Hoja de inspección guardada correctamente"
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

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun clearSuccessMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }
}