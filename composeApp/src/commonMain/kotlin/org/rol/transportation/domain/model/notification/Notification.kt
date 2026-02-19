package org.rol.transportation.domain.model.notification

data class Notification(
    val id: Int,
    val titulo: String,
    val mensaje: String,
    val tipo: String,
    val creadoEn: String,
    val leido: Boolean
)

