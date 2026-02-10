package org.rol.transportation.data.remote.dto.seat_belts

import kotlinx.serialization.Serializable

@Serializable
data class SeatBeltsDto(
    val viajeId: Int,
    val vehiculoId: Int,
    val version: String?,
    val viajeTipo: String?,
    val document: Map<String, SeatBeltItemDto>
)