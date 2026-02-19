package org.rol.transportation.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.rol.transportation.data.remote.dto.driver_document.DriverDetailResponse
import org.rol.transportation.utils.Constants

class DriverApi(private val client: HttpClient) {

    suspend fun getDriverById(id: Int): DriverDetailResponse {
        return client.get("${Constants.DRIVER_ENDPOINT}/find-one/$id") {
            contentType(ContentType.Application.Json)
        }.body()
    }
}
