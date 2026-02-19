package org.rol.transportation.data.remote.dto.notification

import kotlinx.serialization.Serializable

@Serializable
data class NotificationMetaDto(
    val total: Int,
    val page: Int,
    val limit: Int,
    val totalPages: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean
)