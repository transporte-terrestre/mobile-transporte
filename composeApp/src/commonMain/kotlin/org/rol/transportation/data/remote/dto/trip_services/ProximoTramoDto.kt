package org.rol.transportation.data.remote.dto.trip_services

import kotlinx.serialization.Serializable

@Serializable
data class ProximoTramoDto(
    val tipo: String,
    val nombreLugar: String? = null,
    val latitud: String? = null,
    val longitud: String? = null,
    val ultimoKilometraje: Double? = null,
    val ultimaHora: String? = null,
    val ultimosPasajeros: Int? = null,
    val esPuntoFijo: Boolean? = null,
    val rutaParadaId: Int? = null,
    val faltanPuntosFijos: Boolean? = null
)
