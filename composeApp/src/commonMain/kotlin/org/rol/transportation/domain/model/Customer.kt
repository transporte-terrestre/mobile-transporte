package org.rol.transportation.domain.model

data class Customer(
    val id: Int,
    val nombreCompleto: String,
    val email: String?,
    val telefono: String?,
    val direccion: String?,
    val imagenes: List<String> = emptyList()
)
