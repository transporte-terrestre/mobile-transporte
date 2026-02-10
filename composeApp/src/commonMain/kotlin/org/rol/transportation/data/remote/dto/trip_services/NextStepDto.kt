package org.rol.transportation.data.remote.dto.trip_services

import kotlinx.serialization.Serializable

@Serializable
data class NextStepDto(
    val orden: Int,
    val paradaPartidaId: Int? = null,
    val paradaPartidaNombre: String? = null,
    val paradaLlegadaId: Int? = null,
    val paradaLlegadaNombre: String? = null,
    val horaSalida: String? = null,
    val kmInicial: Double? = null,
    val numeroPasajeros: Int? = null,
    val progreso: String? = null,
    val isStart: Boolean? = null,
    val isFinal: Boolean? = null
)