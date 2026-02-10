package org.rol.transportation.data.remote.dto.lights_alarm

import kotlinx.serialization.Serializable

@Serializable
data class LightsAlarmDto(
    val viajeId: Int,
    val vehiculoId: Int,
    val version: String?,
    val viajeTipo: String?,
    val document: Map<String, LightItemDto>
)
