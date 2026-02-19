package org.rol.transportation.domain.model.notification

data class NotificationPage(
    val notifications: List<Notification>,
    val meta: NotificationMeta
)