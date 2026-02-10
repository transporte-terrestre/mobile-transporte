package org.rol.transportation.domain.model.document_inspection

data class DocumentInspection(
    val viajeId: Int?,
    val vehiculoId: Int,
    val version: String?,
    val viajeTipo: String?,
    val document: DocumentInspectionContent
)
