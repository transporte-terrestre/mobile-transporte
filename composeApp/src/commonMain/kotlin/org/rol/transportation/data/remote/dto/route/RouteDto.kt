package org.rol.transportation.data.remote.dto.route

import kotlinx.serialization.Serializable

@Serializable
data class RouteDto(
    val id: Int,
    val origen: String? = null,
    val destino: String? = null,
    val origenLat: String? = null,
    val origenLng: String? = null,
    val destinoLat: String? = null,
    val destinoLng: String? = null,
    val distancia: String? = null,
    val costoBase: String? = null,
    val tiempoEstimado: Int? = null,
    val creadoEn: String? = null,
    val actualizadoEn: String? = null,
    val eliminadoEn: String? = null
)