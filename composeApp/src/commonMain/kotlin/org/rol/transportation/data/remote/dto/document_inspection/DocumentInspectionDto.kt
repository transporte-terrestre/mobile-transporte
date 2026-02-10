package org.rol.transportation.data.remote.dto.document_inspection

import kotlinx.serialization.Serializable

@Serializable
data class DocumentInspectionDto(
    val viajeId: Int,
    val vehiculoId: Int,
    val version: String?,
    val viajeTipo: String?,
    val document: DocumentInspectionContentDto
)