package org.rol.transportation.data.remote.dto.tools_inspection

import kotlinx.serialization.Serializable

@Serializable
data class ToolsInspectionDto(
    val viajeId: Int,
    val vehiculoId: Int,
    val version: String?,
    val viajeTipo: String?,
    val document: ToolsDocumentContentDto
)
