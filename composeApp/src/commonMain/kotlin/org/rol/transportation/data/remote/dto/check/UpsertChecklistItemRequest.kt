package org.rol.transportation.data.remote.dto.check

import kotlinx.serialization.Serializable

@Serializable
data class UpsertChecklistItemRequest(
    val checklistItemId: Int,
    val vehiculoChecklistDocumentId: Int?,
    val observacion: String? = null
)