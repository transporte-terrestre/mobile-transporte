package org.rol.transportation.data.remote.dto.check

import kotlinx.serialization.Serializable

@Serializable
data class ChecklistItemDto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val orden: Int,
    val creadoEn: String,
    val actualizadoEn: String,
    val eliminadoEn: String? = null
)