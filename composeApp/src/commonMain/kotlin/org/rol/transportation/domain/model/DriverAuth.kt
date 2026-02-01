package org.rol.transportation.domain.model

data class DriverAuth(
    val id: Int,
    val dni: String,
    val nombres: String,
    val apellidos: String,
    val nombreCompleto: String,
    val email: String,
    val celular: String?,
    val numeroLicencia: String,
    val claseLicencia: String,
    val categoriaLicencia: String,
    val fotocheck: List<String>
)
