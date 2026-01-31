package org.rol.transportation.data.remote.dto.trip

import kotlinx.serialization.Serializable

@Serializable
data class TripListResponse(
    val data: List<TripDto>,
    val meta: MetaDto
)
