package org.rol.transportation.presentation.driver_documents

import org.rol.transportation.domain.model.driver_documents.DriverDocument

data class DriverDocumentsUiState(
    val isLoading: Boolean = false,
    val documents: List<DriverDocument> = emptyList(),
    val error: String? = null
)
