package org.rol.transportation.domain.model

data class Vehicle(
    val id: Int,
    val marca: String,
    val modelo: String,
    val placa: String,
    val anio: Int,
    val esPrincipal: Boolean,
    val imagenes: List<String> = emptyList()

)