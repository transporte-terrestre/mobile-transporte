package org.rol.transportation.data.remote.dto.tools_inspection

import kotlinx.serialization.Serializable

@Serializable
data class ToolsDocumentContentDto(
    val info: Map<String, String>?, // Para las leyendas de Criterio A, B, etc.
    val herramientas: Map<String, ToolItemDto>
)