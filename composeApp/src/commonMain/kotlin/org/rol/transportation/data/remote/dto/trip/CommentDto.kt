package org.rol.transportation.data.remote.dto.trip

import kotlinx.serialization.Serializable

@Serializable
data class CommentDto(
    val id: Int,
    val viajeId: Int,
    val usuarioId: Int,
    val comentario: String,
    val tipo: String,
    val creadoEn: String? = null,
    val actualizadoEn: String? = null,
    val usuarioNombreCompleto: String? = null
)
