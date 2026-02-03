package org.rol.transportation.data.remote.dto.check

import kotlinx.serialization.Serializable

@Serializable
data class UpsertChecklistItemRequest(
    val id: Int, // checklistItemId
    val completado: Boolean
)