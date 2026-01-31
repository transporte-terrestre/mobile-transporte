package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.User
import org.rol.transportation.utils.Resource

interface AuthRepository {
    suspend fun login(email: String, password: String, rememberSession: Boolean = true): Flow<Resource<User>>
    suspend fun logout()
    fun isLoggedIn(): Boolean
}