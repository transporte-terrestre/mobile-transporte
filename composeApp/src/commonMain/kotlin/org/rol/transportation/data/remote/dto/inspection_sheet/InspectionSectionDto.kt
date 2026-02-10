package org.rol.transportation.data.remote.dto.inspection_sheet

import kotlinx.serialization.Serializable

@Serializable
data class InspectionSectionDto(
    val label: String,
    val items: Map<String, InspectionItemDto>
)
