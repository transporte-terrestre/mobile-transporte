package org.rol.transportation.domain.model.lights_alarm

data class LightsAlarmInspection(
    val viajeId: Int?,
    val vehiculoId: Int,
    val version: String?,
    val viajeTipo: String?,
    val items: Map<String, LightItem>
)
