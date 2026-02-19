package org.rol.transportation.data.remote.dto.notification

import kotlinx.serialization.Serializable

@Serializable
data class NotificationResponse(
    val data: List<NotificationDto>,
    val meta: NotificationMetaDto
)