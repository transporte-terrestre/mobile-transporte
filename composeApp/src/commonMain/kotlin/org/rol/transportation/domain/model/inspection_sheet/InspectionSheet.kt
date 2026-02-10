package org.rol.transportation.domain.model.inspection_sheet

data class InspectionSheet (
    val viajeId: Int?,
    val vehiculoId: Int,
    val version: String?,
    val viajeTipo: String?,
    val document: InspectionSheetDocument
)