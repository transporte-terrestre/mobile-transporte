package org.rol.transportation.data.remote.dto.passenger

import kotlinx.serialization.Serializable

@Serializable
data class PassengerAttendanceDto(
    val viajeId: Int? = null,
    val pasajeroId: Int,
    val asistencia: Boolean,
    val pasajero: PassengerDto? = null
)
