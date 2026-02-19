package org.rol.transportation.domain.repository

import org.rol.transportation.domain.model.notification.NotificationPage

interface NotificationRepository {
    suspend fun getNotifications(conductorId: Int, page: Int, limit: Int): NotificationPage
}
