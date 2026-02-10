package org.rol.transportation.data.remote.dto.inspection_sheet

import kotlinx.serialization.Serializable

@Serializable
data class InspectionItemDto (
    val label: String,
    val color: String,
    val value: Boolean
)