package org.rol.transportation.data.remote.dto.passenger

import kotlinx.serialization.Serializable

@Serializable
data class PassengerUpsertRequest(
    val pasajeros: List<PassengerAttendanceDto>
)
