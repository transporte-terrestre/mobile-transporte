package org.rol.transportation.data.remote.dto.check

import kotlinx.serialization.Serializable

@Serializable
data class CreateChecklistRequest(
    val tipo: String,
    val items: List<ChecklistItemRequest>,
    val observaciones: String? = null
)