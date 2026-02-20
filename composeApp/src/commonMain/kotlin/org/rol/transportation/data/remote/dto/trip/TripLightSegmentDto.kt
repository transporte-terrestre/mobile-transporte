package org.rol.transportation.data.remote.dto.trip

import kotlinx.serialization.Serializable

@Serializable
data class TripLightSegmentDto(
    val id: Int,
    val rutaNombre: String? = null,
    val estado: String? = null,
    val fecha: String? = null,
    val checkInSalida: Boolean = false,
    val checkInLlegada: Boolean = false
)