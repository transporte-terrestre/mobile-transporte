package org.rol.transportation.utils

import org.rol.transportation.BuildKonfig

object Constants {
    val BASE_URL: String get() = BuildKonfig.BASE_URL
    const val LOGIN_ENDPOINT = "auth/conductor/login"
    const val TRIP_ENDPOINT = "viaje/find-all"
    const val TRIP = "viaje"
    const val TRIP_DETAIL_ENDPOINT = "viaje/find-one"
    const val CHECKLIST_ITEMS_ENDPOINT = "viaje/checklist-item/find-all"
    const val TRIP_CHECKLIST_ENDPOINT = "viaje"
    const val VEHICULO_ENDPOINT = "vehiculo"
    const val STORAGE_ENDPOINT = "storage"

}