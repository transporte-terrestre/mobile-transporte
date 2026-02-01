package org.rol.transportation.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.DriverAuth
import org.rol.transportation.domain.model.User
import org.rol.transportation.domain.repository.AuthRepository
import org.rol.transportation.utils.Resource

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String,
                                rememberSession: Boolean = true): Flow<Resource<DriverAuth>> {
        return authRepository.loginDriver(email, password, rememberSession)
    }

}