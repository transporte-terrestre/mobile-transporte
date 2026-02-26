package org.rol.transportation.data.remote.dto.trip_services

import kotlinx.serialization.Serializable

@Serializable
data class RegistrarServicioRequest(
    val longitud: Double?,
    val latitud: Double?,
    val horaActual: String,
    val nombreLugar: String,
    val kilometrajeActual: Double,
    val cantidadPasajeros: Int,
    val rutaParadaId: Int? = null
)
