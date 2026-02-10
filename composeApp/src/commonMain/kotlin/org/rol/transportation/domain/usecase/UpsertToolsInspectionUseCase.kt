package org.rol.transportation.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.tools_inspection.ToolsInspection
import org.rol.transportation.domain.repository.ToolsInspectionRepository
import org.rol.transportation.utils.Resource

class UpsertToolsInspectionUseCase(
    private val repository: ToolsInspectionRepository
) {
    suspend operator fun invoke(
        vehiculoId: Int,
        viajeId: Int,
        viajeTipo: TripType,
        inspection: ToolsInspection
    ): Flow<Resource<ToolsInspection>> {
        return repository.upsertTools(vehiculoId, viajeId, viajeTipo, inspection)
    }
}