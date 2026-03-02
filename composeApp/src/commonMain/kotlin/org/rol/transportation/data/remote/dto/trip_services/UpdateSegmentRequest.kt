package org.rol.transportation.data.remote.dto.trip_services

import kotlinx.serialization.Serializable

@Serializable
data class UpdateSegmentRequest(
    val nombreLugar: String? = null,
    val horaFinal: String? = null,
    val kilometrajeFinal: Double? = null,
    val numeroPasajeros: Int? = null
)
