package org.rol.transportation.domain.model.document_inspection

data class DocumentSection(
    val label: String,
    val items: Map<String, DocumentItem>
)