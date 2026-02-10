package org.rol.transportation.domain.model.seat_belts

data class SeatBeltsInspection(
    val viajeId: Int?,
    val vehiculoId: Int,
    val version: String?,
    val viajeTipo: String?,
    val items: Map<String, SeatBeltItem>
)
