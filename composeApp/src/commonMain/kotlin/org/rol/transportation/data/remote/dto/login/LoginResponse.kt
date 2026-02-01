package org.rol.transportation.data.remote.dto.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val accessToken: String,
    val conductor: DriverDto
)