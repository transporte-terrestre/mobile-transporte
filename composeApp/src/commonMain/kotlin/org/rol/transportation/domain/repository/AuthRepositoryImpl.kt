package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.rol.transportation.data.local.TokenManager
import org.rol.transportation.data.remote.api.AuthApi
import org.rol.transportation.domain.model.User
import org.rol.transportation.utils.Resource

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(email: String, password: String, rememberSession: Boolean): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading)

            val response = authApi.login(email, password)

            // Solo guardar si rememberSession es true
            if (rememberSession) {

                tokenManager.saveToken(response.accessToken)

                tokenManager.saveUserData(
                    userId = response.user.id,
                    email = response.user.email,
                    name = response.user.nombreCompleto
                )
            }

            // Mapear a Domain Model
            val user = User(
                id = response.user.id,
                nombres = response.user.nombres,
                apellidos = response.user.apellidos,
                nombreCompleto = response.user.nombreCompleto,
                email = response.user.email,
                roles = response.user.roles,
                fotocheck = response.user.fotocheck,
                creadoEn = response.user.creadoEn,
                actualizadoEn = response.user.actualizadoEn
            )

            emit(Resource.Success(user))

        } catch (e: Exception) {
            emit(Resource.Error(
                message = e.message ?: "Error desconocido al iniciar sesión",
                throwable = e
            ))
        }
    }

    override suspend fun logout() {
        tokenManager.clearAll()
    }

    override fun isLoggedIn(): Boolean {
        return tokenManager.isLoggedIn()
    }
}