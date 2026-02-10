package org.rol.transportation.domain.model.inspection_sheet

data class InspectionSheetDocument (
    val declaracionJurada: InspectionSection,
    val estadoGeneral: InspectionSection,
    val estadoInterno: InspectionSection,
    val elementosSeguridad: InspectionSection,
    val estadoMecanico: InspectionSection,
    val sistemasCriticos: InspectionSection,
    val cinturonesSeguridad: InspectionSection
)