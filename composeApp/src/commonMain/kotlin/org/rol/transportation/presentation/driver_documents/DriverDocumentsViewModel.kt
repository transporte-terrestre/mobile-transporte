package org.rol.transportation.presentation.driver_documents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.rol.transportation.domain.usecase.GetDriverDocumentsUseCase

class DriverDocumentsViewModel(
    private val getDriverDocumentsUseCase: GetDriverDocumentsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DriverDocumentsUiState())
    val uiState: StateFlow<DriverDocumentsUiState> = _uiState.asStateFlow()

    init {
        loadDocuments()
    }

    fun loadDocuments() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val driverDetail = getDriverDocumentsUseCase()
                _uiState.update { it.copy(isLoading = false, documents = driverDetail.documentos) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Ocurrió un error inesperado") }
            }
        }
    }
}
