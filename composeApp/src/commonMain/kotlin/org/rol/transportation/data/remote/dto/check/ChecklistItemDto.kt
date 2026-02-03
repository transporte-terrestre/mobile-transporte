package org.rol.transportation.data.remote.dto.check

import kotlinx.serialization.Serializable

@Serializable
data class ChecklistItemDto(
    val id: Int,
    val seccion: String,
    val nombre: String,
    val descripcion: String,
    val icono: String? = null,
    val orden: Int,
    val activo: Boolean,
    val creadoEn: String,
    val actualizadoEn: String,
    val eliminadoEn: String? = null
)