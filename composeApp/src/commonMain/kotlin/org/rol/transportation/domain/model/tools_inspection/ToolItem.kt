package org.rol.transportation.domain.model.tools_inspection

data class ToolItem(
    val label: String,
    val estado: Boolean,
    val stock: String,
    val criterioA: Boolean,
    val criterioB: Boolean,
    val criterioC: Boolean,
    val criterioD: Boolean,
    val criterioE: Boolean,
    val criterioF: Boolean,
    val accionCorrectiva: String,
    val observacion: String
)