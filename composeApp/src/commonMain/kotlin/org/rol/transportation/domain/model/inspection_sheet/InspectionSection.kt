package org.rol.transportation.domain.model.inspection_sheet

data class InspectionSection (
    val label: String,
    val items: Map<String, InspectionItem>
)