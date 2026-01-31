package org.rol.transportation.domain.model

data class Driver(
    val id: Int,
    val nombreCompleto: String,
    val numeroLicencia: String,
    val rol: String,
    val esPrincipal: Boolean,
    val fotocheck: List<String> = emptyList()
)
