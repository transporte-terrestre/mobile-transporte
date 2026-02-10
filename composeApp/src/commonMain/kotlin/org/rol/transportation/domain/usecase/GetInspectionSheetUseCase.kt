package org.rol.transportation.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.inspection_sheet.InspectionSheet
import org.rol.transportation.domain.repository.InspectionSheetRepository
import org.rol.transportation.utils.Resource

class GetInspectionSheetUseCase (
    private val repository: InspectionSheetRepository
) {
    suspend operator fun invoke(
        vehiculoId: Int,
        documentId: Int?
    ): Flow<Resource<InspectionSheet>> {
        return repository.getInspectionSheet(vehiculoId, documentId)
    }
}