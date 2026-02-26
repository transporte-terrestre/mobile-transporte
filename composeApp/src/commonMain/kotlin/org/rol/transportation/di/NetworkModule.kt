package org.rol.transportation.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.rol.transportation.data.local.TokenManager
import org.rol.transportation.utils.Constants

val networkModule = module {
    single {
        val tokenManager: TokenManager = get()

        HttpClient {
            expectSuccess = true
            
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                })
            }

            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }

            defaultRequest {
                url(Constants.BASE_URL)
                val token = tokenManager.getToken()
                if (!token.isNullOrEmpty()) {
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            }
        }
    }
}