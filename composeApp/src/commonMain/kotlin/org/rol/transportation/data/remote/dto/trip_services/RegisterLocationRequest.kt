package org.rol.transportation.data.remote.dto.trip_services

import kotlinx.serialization.Serializable

@Serializable
data class RegisterLocationRequest(
    val longitud: Double,
    val latitud: Double,
    val cantidadPasajeros: Int,
    val horaActual: String,
    val kilometrajeActual: Double,
    val nombreLugar: String,
    val rutaParadaId: Int? = null
)