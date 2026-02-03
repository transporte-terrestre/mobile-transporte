package org.rol.transportation.data.remote.dto.check

import kotlinx.serialization.Serializable

@Serializable
data class UpsertChecklistRequest(
    val observaciones: String? = null,
    val items: List<UpsertChecklistItemRequest>
)