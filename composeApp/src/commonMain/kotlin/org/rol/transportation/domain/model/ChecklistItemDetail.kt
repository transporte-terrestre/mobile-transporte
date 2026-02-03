package org.rol.transportation.domain.model

data class ChecklistItemDetail(
    val checklistItemId: Int,
    val nombre: String,
    val descripcion: String,
    val completado: Boolean,
    val seccion: String,
    val orden: Int
)