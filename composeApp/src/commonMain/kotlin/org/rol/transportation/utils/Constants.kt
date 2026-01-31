package org.rol.transportation.utils

import org.rol.transportation.BuildKonfig

object Constants {
    val BASE_URL: String get() = BuildKonfig.BASE_URL
    const val LOGIN_ENDPOINT = "auth/login"
    const val TRIP_ENDPOINT = "viaje/find-all"
    const val TRIP_DETAIL_ENDPOINT = "viaje/find-one"

}