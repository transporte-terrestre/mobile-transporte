package org.rol.transportation.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.rol.transportation.data.remote.dto.trip.TripDto
import org.rol.transportation.data.remote.dto.trip.TripListResponse
import org.rol.transportation.data.remote.dto.trip.TripSegmentDto
import org.rol.transportation.utils.Constants

class TripApi(private val client: HttpClient) {

    suspend fun getTrips(
        page: Int = 1,
        limit: Int = 10,
        search: String? = null
    ): TripListResponse {
        return client.get(Constants.TRIP_ENDPOINT) {
            parameter("page", page)
            parameter("limit", limit)
            search?.let { parameter("search", it) }
        }.body()
    }

    suspend fun getLightTrips(
        page: Int = 1,
        limit: Int = 10
    ): org.rol.transportation.data.remote.dto.trip.TripLightListResponse {
        return client.get(Constants.TRIP_LIGHT_ENDPOINT) {
            parameter("page", page)
            parameter("limit", limit)
        }.body()
    }

    suspend fun getTripById(id: Int): TripSegmentDto {
        return client.get("${Constants.TRIP_DETAIL_ENDPOINT}/$id")
            .body()
    }
}