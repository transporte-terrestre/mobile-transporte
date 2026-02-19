package org.rol.transportation.data.remote.dto.notification

import kotlinx.serialization.Serializable


@Serializable
data class NotificationDto(
    val id: Int,
    val titulo: String,
    val mensaje: String,
    val tipo: String,
    val creadoEn: String,
    val leido: Boolean
)


