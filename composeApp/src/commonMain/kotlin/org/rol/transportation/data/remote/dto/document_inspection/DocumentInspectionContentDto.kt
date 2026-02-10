package org.rol.transportation.data.remote.dto.document_inspection

import kotlinx.serialization.Serializable

@Serializable
data class DocumentInspectionContentDto(
    val documentosVehiculo: DocumentSectionDto,
    val documentosConductor: DocumentSectionDto
)
