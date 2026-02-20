package org.rol.transportation.domain.model

data class TripLightSegment(
    val id: Int,
    val rutaNombre: String,
    val estado: String,
    val fecha: String,
    val checkInSalida: Boolean,
    val checkInLlegada: Boolean
)