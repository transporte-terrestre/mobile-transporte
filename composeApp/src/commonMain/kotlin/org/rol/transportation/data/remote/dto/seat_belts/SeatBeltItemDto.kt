package org.rol.transportation.data.remote.dto.seat_belts

import kotlinx.serialization.Serializable

@Serializable
data class SeatBeltItemDto(
    val label: String,
    val habilitado: Boolean,
    val observacion: String?
)