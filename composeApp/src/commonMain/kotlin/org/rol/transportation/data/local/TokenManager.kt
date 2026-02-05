package org.rol.transportation.data.local

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

class TokenManager(private val settings: Settings) {

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_PHOTO = "user_photo"

        private const val KEY_DRIVER_DNI = "driver_dni"
        private const val KEY_DRIVER_LICENSE = "driver_license"
    }

    fun saveToken(token: String) {
        settings[KEY_ACCESS_TOKEN] = token
    }

    fun getToken(): String? {
        return settings.getStringOrNull(KEY_ACCESS_TOKEN)
    }

    fun saveUserData(userId: Int, email: String, name: String, photoUrl: String?) {
        settings[KEY_USER_ID] = userId
        settings[KEY_USER_EMAIL] = email
        settings[KEY_USER_NAME] = name
        settings[KEY_USER_PHOTO] = photoUrl ?: ""
    }

    fun saveDriverData(dni: String, licenseNumber: String) {
        settings[KEY_DRIVER_DNI] = dni
        settings[KEY_DRIVER_LICENSE] = licenseNumber
    }

    fun getUserId(): Int? {
        return settings.getIntOrNull(KEY_USER_ID)
    }

    fun getUserEmail(): String? {
        return settings.getStringOrNull(KEY_USER_EMAIL)
    }

    fun getUserName(): String? {
        return settings.getStringOrNull(KEY_USER_NAME)
    }

    fun getUserPhoto(): String? {
        return settings.getStringOrNull(KEY_USER_PHOTO)
    }

    fun getDriverDni(): String? {
        return settings.getStringOrNull(KEY_DRIVER_DNI)
    }

    fun getLicenseDriver(): String? {
        return settings.getStringOrNull(KEY_DRIVER_LICENSE)
    }

    fun isLoggedIn(): Boolean {
        return getToken() != null
    }

    fun clearAll() {
        settings.clear()
    }
}