package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.rol.transportation.data.local.TokenManager
import org.rol.transportation.data.remote.api.AuthApi
import org.rol.transportation.domain.model.DriverAuth
import org.rol.transportation.utils.Resource

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun loginDriver(email: String, password: String, rememberSession: Boolean): Flow<Resource<DriverAuth>> = flow {
        try {
            emit(Resource.Loading)

            val response = authApi.login(email, password)

            // Solo guardar si rememberSession es true
            if (rememberSession) {

                tokenManager.saveToken(response.accessToken)

                val photoUrl = response.conductor.fotocheck.firstOrNull()

                tokenManager.saveUserData(
                    userId = response.conductor.id,
                    email = response.conductor.email,
                    name = response.conductor.nombreCompleto,
                    photoUrl = photoUrl
                )

                tokenManager.saveDriverData(
                    dni = response.conductor.dni,
                    licenseNumber = response.conductor.numeroLicencia
                )
            }

            // Mapear a Domain Model
            val driver = DriverAuth(
                id = response.conductor.id,
                dni = response.conductor.dni,
                nombres = response.conductor.nombres,
                apellidos = response.conductor.apellidos,
                nombreCompleto = response.conductor.nombreCompleto,
                email = response.conductor.email,
                celular = response.conductor.celular,
                numeroLicencia = response.conductor.numeroLicencia,
                claseLicencia = response.conductor.claseLicencia,
                categoriaLicencia = response.conductor.categoriaLicencia,
                fotocheck = response.conductor.fotocheck
            )

            emit(Resource.Success(driver))

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