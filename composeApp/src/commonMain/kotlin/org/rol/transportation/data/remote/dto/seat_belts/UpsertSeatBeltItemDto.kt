package org.rol.transportation.data.remote.dto.seat_belts

import kotlinx.serialization.Serializable

@Serializable
data class UpsertSeatBeltItemDto(
    val habilitado: Boolean,
    val observacion: String?
)