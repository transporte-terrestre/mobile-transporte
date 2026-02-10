package org.rol.transportation.data.remote.dto.spill_kit

import kotlinx.serialization.Serializable

@Serializable
data class SpillKitItemDto(
    val label: String,
    val estado: Boolean
)
