package org.rol.transportation.data.remote.dto.login

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int,
    val nombres: String,
    val apellidos: String,
    val nombreCompleto: String,
    val email: String,
    val roles: List<String>,
    val fotocheck: List<String> = emptyList(),
    val creadoEn: String,
    val actualizadoEn: String,
    val eliminadoEn: String? = null
)