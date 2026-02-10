package org.rol.transportation.data.remote.dto.passenger

import kotlinx.serialization.Serializable

@Serializable
data class PassengerDto(
    val id: Int,
    val dni: String,
    val nombres: String,
    val apellidos: String
)