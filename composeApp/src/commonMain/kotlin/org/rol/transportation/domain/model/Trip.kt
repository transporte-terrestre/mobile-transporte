package org.rol.transportation.domain.model


data class Trip(
    val id: Int,
    val creadoEn: String,
    val ida: TripSegment,
    val vuelta: TripSegment? = null
)

