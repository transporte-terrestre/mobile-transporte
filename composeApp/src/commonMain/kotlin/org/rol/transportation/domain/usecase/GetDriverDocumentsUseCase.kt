package org.rol.transportation.domain.usecase

import org.rol.transportation.data.local.TokenManager
import org.rol.transportation.domain.repository.DriverRepository

class GetDriverDocumentsUseCase(
    private val driverRepository: DriverRepository,
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke() = driverRepository.getDriverWithDocuments(tokenManager.getUserId()!!)
}
