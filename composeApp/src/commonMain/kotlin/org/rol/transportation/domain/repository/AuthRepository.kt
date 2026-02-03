package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.DriverAuth
import org.rol.transportation.utils.Resource

interface AuthRepository {
    suspend fun loginDriver(email: String, password: String, rememberSession: Boolean = true): Flow<Resource<DriverAuth>>
    suspend fun logout()
    fun isLoggedIn(): Boolean
}