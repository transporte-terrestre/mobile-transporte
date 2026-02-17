package org.rol.transportation.domain.model

data class Notification(
    val id: Int,
    val titulo: String,
    val mensaje: String,
    val tipo: String,
    val creadoEn: String,
    val leido: Boolean
)

data class NotificationMeta(
    val total: Int,
    val page: Int,
    val limit: Int,
    val totalPages: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean
)

data class NotificationPage(
    val notifications: List<Notification>,
    val meta: NotificationMeta
)
