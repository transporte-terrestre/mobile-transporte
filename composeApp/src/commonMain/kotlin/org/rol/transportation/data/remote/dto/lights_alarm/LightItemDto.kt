package org.rol.transportation.data.remote.dto.lights_alarm

import kotlinx.serialization.Serializable

@Serializable
data class LightItemDto(
    val label: String,
    val estado: Boolean,
    val observacion: String?
)
