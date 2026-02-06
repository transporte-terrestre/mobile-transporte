package org.rol.transportation.data.remote.dto.check

import kotlinx.serialization.Serializable

@Serializable
data class UpsertChecklistRequest(
    val items: List<UpsertChecklistItemRequest>
)