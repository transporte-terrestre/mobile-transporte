package org.rol.transportation.data.remote.dto.trip_services

import kotlinx.serialization.Serializable

@Serializable
data class SegmentDto(
    val id: Int,
    val viajeId: Int,
    val tipo: String,
    val rutaParadaId: Int? = null,
    val longitud: Double? = null,
    val latitud: Double? = null,
    val nombreLugar: String? = null,
    val horaFinal: String? = null,
    val kilometrajeFinal: Double? = null,
    val numeroPasajeros: Int? = null,
    val creadoEn: String? = null,
    val actualizadoEn: String? = null
)