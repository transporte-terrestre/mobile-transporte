package org.rol.transportation.domain.model

data class User(
    val id: Int,
    val nombres: String,
    val apellidos: String,
    val nombreCompleto: String,
    val email: String,
    val roles: List<String>,
    val fotocheck: List<String>,
    val creadoEn: String,
    val actualizadoEn: String
)