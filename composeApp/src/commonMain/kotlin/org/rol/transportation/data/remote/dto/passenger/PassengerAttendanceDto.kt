package org.rol.transportation.data.remote.dto.passenger

import kotlinx.serialization.Serializable

@Serializable
data class PassengerAttendanceDto(
    val id: Int? = null,
    val viajeId: Int? = null,
    val pasajeroId: Int? = null,
    val dni: String? = null,
    val nombres: String? = null,
    val apellidos: String? = null,
    val asistencia: Boolean,
    val pasajero: PassengerDto? = null
)
