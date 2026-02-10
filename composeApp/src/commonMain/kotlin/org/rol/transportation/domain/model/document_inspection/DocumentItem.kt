package org.rol.transportation.domain.model.document_inspection

data class DocumentItem(
    val label: String,
    val habilitado: Boolean,
    val fechaVencimiento: String,
    val observacion: String
)
