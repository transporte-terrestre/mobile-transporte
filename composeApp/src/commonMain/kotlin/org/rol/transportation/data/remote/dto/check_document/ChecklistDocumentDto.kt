package org.rol.transportation.data.remote.dto.check_document

import kotlinx.serialization.Serializable

@Serializable
data class ChecklistDocumentDto(
    val viajeId: Int? = null,
    val vehiculoId: Int,
    val version: String? = null,
    val viajeTipo: String? = null,
    val document: DocumentContentDto
)