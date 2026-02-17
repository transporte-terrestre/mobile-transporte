package org.rol.transportation.data.remote.dto.lights_alarm

import kotlinx.serialization.Serializable

@Serializable
data class LightsAlarmDto(
    val viajeId: Int? = null,
    val vehiculoId: Int,
    val version: String? = null,
    val viajeTipo: String? = null,
    val document: Map<String, LightItemDto>
)
