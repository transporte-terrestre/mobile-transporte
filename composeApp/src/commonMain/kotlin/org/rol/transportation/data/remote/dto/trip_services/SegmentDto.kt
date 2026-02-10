package org.rol.transportation.data.remote.dto.trip_services

import kotlinx.serialization.Serializable

@Serializable
data class SegmentDto(
    val id: Int,
    val viajeId: Int,
    val orden: Int,
    val paradaPartidaId: Int,
    val paradaPartidaNombre: String,
    val paradaLlegadaId: Int?,
    val paradaLlegadaNombre: String?,
    val horaSalida: String,
    val horaTermino: String?,
    val kmInicial: Double,
    val kmFinal: Double?,
    val numeroPasajeros: Int?,
    val observaciones: String?,
    val kmServicio: Double? = null,
    val tiempoServicioMinutos: Int? = null
)