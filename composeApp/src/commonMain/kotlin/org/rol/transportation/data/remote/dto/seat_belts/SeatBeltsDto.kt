package org.rol.transportation.data.remote.dto.seat_belts

import kotlinx.serialization.Serializable

@Serializable
data class SeatBeltsDto(
    val viajeId: Int? = null,
    val vehiculoId: Int,
    val version: String? = null,
    val viajeTipo: String? = null,
    val document: Map<String, SeatBeltItemDto>
)