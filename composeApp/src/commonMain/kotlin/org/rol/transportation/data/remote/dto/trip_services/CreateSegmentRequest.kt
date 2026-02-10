package org.rol.transportation.data.remote.dto.trip_services

import kotlinx.serialization.Serializable

@Serializable
data class CreateSegmentRequest(
    val paradaPartidaId: Int,
    val paradaLlegadaId: Int,
    val horaSalida: String,
    val horaTermino: String,
    val kmInicial: Double,
    val kmFinal: Double,
    val numeroPasajeros: Int,
    val observaciones: String
)