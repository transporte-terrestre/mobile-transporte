package org.rol.transportation.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.tools_inspection.ToolsInspection
import org.rol.transportation.domain.repository.ToolsInspectionRepository
import org.rol.transportation.utils.Resource

class GetToolsInspectionUseCase(
    private val repository: ToolsInspectionRepository
) {
    suspend operator fun invoke(
        vehiculoId: Int,
        documentId: Int?
    ): Flow<Resource<ToolsInspection>> {
        return repository.getTools(vehiculoId, documentId)
    }
}