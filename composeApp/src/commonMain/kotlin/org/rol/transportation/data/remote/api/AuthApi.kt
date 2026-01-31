package org.rol.transportation.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.rol.transportation.data.remote.dto.login.LoginRequest
import org.rol.transportation.data.remote.dto.login.LoginResponse
import org.rol.transportation.utils.Constants

class AuthApi(private val client: HttpClient) {

    suspend fun login(email: String, password: String): LoginResponse {
        return client.post(Constants.LOGIN_ENDPOINT) {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, password))
        }.body()
    }
}