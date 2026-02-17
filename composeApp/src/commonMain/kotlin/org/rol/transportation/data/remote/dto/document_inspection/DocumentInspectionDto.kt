package org.rol.transportation.data.remote.dto.document_inspection

import kotlinx.serialization.Serializable

@Serializable
data class DocumentInspectionDto(
    val viajeId: Int? = null,
    val vehiculoId: Int,
    val version: String? = null,
    val viajeTipo: String? = null,
    val document: DocumentInspectionContentDto
)