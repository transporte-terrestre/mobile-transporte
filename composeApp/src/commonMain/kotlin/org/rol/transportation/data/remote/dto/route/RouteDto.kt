package org.rol.transportation.data.remote.dto.route

import kotlinx.serialization.Serializable

@Serializable
data class RouteDto(
    val id: Int,
    val origen: String,
    val destino: String,
    val origenLat: String,
    val origenLng: String,
    val destinoLat: String,
    val destinoLng: String,
    val distancia: String,
    val costoBase: String,
    val creadoEn: String,
    val actualizadoEn: String,
    val eliminadoEn: String? = null
)