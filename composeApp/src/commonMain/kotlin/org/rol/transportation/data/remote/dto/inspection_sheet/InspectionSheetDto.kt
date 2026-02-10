package org.rol.transportation.data.remote.dto.inspection_sheet

import kotlinx.serialization.Serializable

@Serializable
data class InspectionSheetDto(
    val viajeId: Int? = null,
    val vehiculoId: Int,
    val version: String? = null,
    val viajeTipo: String? = null,
    val document: InspectionSheetDocumentDto
)
