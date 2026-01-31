package org.rol.transportation.domain.model

data class Route(
    val id: Int,
    val origen: String,
    val destino: String,
    val origenLat: Double,
    val origenLng: Double,
    val destinoLat: Double,
    val destinoLng: Double,
    val distancia: String
)