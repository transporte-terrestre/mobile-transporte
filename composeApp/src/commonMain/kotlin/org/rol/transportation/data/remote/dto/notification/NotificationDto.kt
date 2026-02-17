package org.rol.transportation.data.remote.dto.notification

import kotlinx.serialization.Serializable

@Serializable
data class NotificationResponse(
    val data: List<NotificationDto>,
    val meta: NotificationMetaDto
)

@Serializable
data class NotificationDto(
    val id: Int,
    val titulo: String,
    val mensaje: String,
    val tipo: String,
    val creadoEn: String,
    val leido: Boolean
)

@Serializable
data class NotificationMetaDto(
    val total: Int,
    val page: Int,
    val limit: Int,
    val totalPages: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean
)
