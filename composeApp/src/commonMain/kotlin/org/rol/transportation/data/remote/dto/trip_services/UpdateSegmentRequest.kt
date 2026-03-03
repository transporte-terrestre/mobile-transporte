package org.rol.transportation.data.remote.dto.trip_services

import kotlinx.serialization.Serializable

@Serializable
data class UpdateSegmentRequest(
    val tipo: String? = null,
    val longitud: Double? = null,
    val latitud: Double? = null,
    val nombreLugar: String? = null,
    val horaFinal: String? = null,
    val kilometrajeFinal: Double? = null,
    val numeroPasajeros: Int? = null
)
