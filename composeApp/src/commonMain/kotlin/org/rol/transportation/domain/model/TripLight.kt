package org.rol.transportation.domain.model

data class TripLight(
    val id: Int,
    val ida: TripLightSegment,
    val vuelta: TripLightSegment?
)

