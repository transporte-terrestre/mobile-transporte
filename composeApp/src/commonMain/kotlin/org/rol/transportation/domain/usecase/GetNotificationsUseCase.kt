package org.rol.transportation.domain.usecase

import org.rol.transportation.data.local.TokenManager
import org.rol.transportation.domain.repository.NotificationRepository

class GetNotificationsUseCase(
    private val notificationRepository: NotificationRepository,
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke(page: Int, limit: Int) = notificationRepository.getNotifications(
        conductorId = tokenManager.getUserId()!!,
        page = page,
        limit = limit
    )
}
