package org.rol.transportation.domain.model.document_inspection

data class DocumentInspectionContent(
    val documentosVehiculo: DocumentSection,
    val documentosConductor: DocumentSection
)
