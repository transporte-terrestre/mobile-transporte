package org.rol.transportation.data.remote.dto.trip

import kotlinx.serialization.Serializable

@Serializable
data class TripLightListResponse(
    val data: List<TripLightDto>,
    val meta: MetaDto
)