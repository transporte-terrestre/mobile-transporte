package org.rol.transportation.domain.model

data class ChecklistItem(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val orden: Int,
    val creadoEn: String,
    val actualizadoEn: String,
    val eliminadoEn: String?
)
