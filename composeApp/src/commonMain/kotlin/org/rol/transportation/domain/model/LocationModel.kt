package org.rol.transportation.domain.model

import kotlin.time.Clock

data class LocationModel(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float? = null,
    val timestamp: Long = Clock.System.now().toEpochMilliseconds()
)
