package org.rol.transportation.data.remote.dto.check

import kotlinx.serialization.Serializable

@Serializable
data class ChecklistItemRequest(
    val checklistItemId: Int,
    val completado: Boolean,
    val observacion: String? = null
)
