package org.rol.transportation.data.remote.dto.inspection_sheet

import kotlinx.serialization.Serializable

@Serializable
data class InspectionSheetDocumentDto (
    val declaracionJurada: InspectionSectionDto,
    val estadoGeneral: InspectionSectionDto,
    val estadoInterno: InspectionSectionDto,
    val elementosSeguridad: InspectionSectionDto,
    val estadoMecanico: InspectionSectionDto,
    val sistemasCriticos: InspectionSectionDto,
    val cinturonesSeguridad: InspectionSectionDto
)
