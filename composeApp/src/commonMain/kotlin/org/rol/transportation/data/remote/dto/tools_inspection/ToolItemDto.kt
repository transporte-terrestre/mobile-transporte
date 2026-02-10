package org.rol.transportation.data.remote.dto.tools_inspection

import kotlinx.serialization.Serializable

@Serializable
data class ToolItemDto(
    val label: String,
    val estado: Boolean,
    val stock: String?,
    val criterioA: Boolean,
    val criterioB: Boolean,
    val criterioC: Boolean,
    val criterioD: Boolean,
    val criterioE: Boolean,
    val criterioF: Boolean,
    val accionCorrectiva: String?,
    val observacion: String?
)
