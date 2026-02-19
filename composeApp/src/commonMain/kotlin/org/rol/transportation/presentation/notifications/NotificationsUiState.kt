package org.rol.transportation.presentation.notifications

import org.rol.transportation.domain.model.notification.Notification

data class NotificationsUiState(
    val isLoading: Boolean = false,
    val notifications: List<Notification> = emptyList(),
    val error: String? = null,
    val page: Int = 1,
    val hasNextPage: Boolean = false
)
