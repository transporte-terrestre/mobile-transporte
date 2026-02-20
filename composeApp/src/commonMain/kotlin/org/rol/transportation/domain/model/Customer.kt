package org.rol.transportation.domain.model

data class Customer(
    val id: Int,
    val nombreCompleto: String,
    val ruc: String? = null,
    val razonSocial: String? = null,
    val email: String?,
    val telefono: String?,
    val direccion: String?,
    val imagenes: List<String> = emptyList()
)
