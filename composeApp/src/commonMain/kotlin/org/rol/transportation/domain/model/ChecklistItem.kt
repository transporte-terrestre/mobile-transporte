package org.rol.transportation.domain.model

data class ChecklistItem(
    val id: Int,
    val seccion: String,
    val nombre: String,
    val descripcion: String,
    val icono: String?,
    val orden: Int
)