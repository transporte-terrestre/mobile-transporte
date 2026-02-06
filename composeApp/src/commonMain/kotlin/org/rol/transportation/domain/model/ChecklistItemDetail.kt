package org.rol.transportation.domain.model

data class ChecklistItemDetail(
    val checklistItemId: Int,
    val vehiculoChecklistDocumentId: Int?,
    val nombre: String,
    val descripcion: String,
    val orden: Int,
    val observacion: String?,
    val creadoEn: String?,
    val actualizadoEn: String?
)