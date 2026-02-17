package org.rol.transportation.domain.repository

import org.rol.transportation.data.remote.api.NotificationApi
import org.rol.transportation.domain.model.Notification
import org.rol.transportation.domain.model.NotificationMeta
import org.rol.transportation.domain.model.NotificationPage

class NotificationRepositoryImpl(private val api: NotificationApi) : NotificationRepository {
    override suspend fun getNotifications(conductorId: Int, page: Int, limit: Int): NotificationPage {
        val response = api.getNotifications(conductorId, page, limit)
        return NotificationPage(
            notifications = response.data.map {
                Notification(
                    id = it.id,
                    titulo = it.titulo,
                    mensaje = it.mensaje,
                    tipo = it.tipo,
                    creadoEn = it.creadoEn,
                    leido = it.leido
                )
            },
            meta = NotificationMeta(
                total = response.meta.total,
                page = response.meta.page,
                limit = response.meta.limit,
                totalPages = response.meta.totalPages,
                hasNextPage = response.meta.hasNextPage,
                hasPreviousPage = response.meta.hasPreviousPage
            )
        )
    }
}
