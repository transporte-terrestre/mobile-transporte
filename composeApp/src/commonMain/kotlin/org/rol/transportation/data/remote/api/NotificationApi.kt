package org.rol.transportation.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.rol.transportation.data.remote.dto.notification.NotificationResponse
import org.rol.transportation.utils.Constants

class NotificationApi(private val client: HttpClient) {

    suspend fun getNotifications(
        conductorId: Int,
        page: Int,
        limit: Int
    ): NotificationResponse {
        return client.get("${Constants.NOTIFICATION_ENDPOINT}/conductor/$conductorId") {
            parameter("page", page)
            parameter("limit", limit)
        }.body()
    }
}
