package org.rol.transportation.data.remote.dto.trip

import kotlinx.serialization.Serializable

@Serializable
data class TripDto(
    val id: Int,
    val creadoEn: String,
    val actualizadoEn: String,
    val eliminadoEn: String? = null,
    val ida: TripSegmentDto,
    val vuelta: TripSegmentDto? = null
)