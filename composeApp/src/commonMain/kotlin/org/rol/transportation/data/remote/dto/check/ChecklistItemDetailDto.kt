package org.rol.transportation.data.remote.dto.check

import kotlinx.serialization.Serializable

@Serializable
data class ChecklistItemDetailDto(
    val viajeChecklistId: Int,
    val checklistItemId: Int,
    val completado: Boolean,
    val observacion: String? = null,
    val creadoEn: String,
    val actualizadoEn: String,
    val seccion: String,
    val nombre: String,
    val descripcion: String,
    val icono: String? = null,
    val orden: Int
)