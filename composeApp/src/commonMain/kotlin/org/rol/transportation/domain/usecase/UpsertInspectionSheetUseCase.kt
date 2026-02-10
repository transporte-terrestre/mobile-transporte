package org.rol.transportation.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.inspection_sheet.InspectionSheet
import org.rol.transportation.domain.repository.InspectionSheetRepository
import org.rol.transportation.utils.Resource

class UpsertInspectionSheetUseCase (
    private val repository: InspectionSheetRepository
) {
    suspend operator fun invoke(
        vehiculoId: Int,
        viajeId: Int,
        viajeTipo: TripType,
        hojaInspeccion: InspectionSheet
    ): Flow<Resource<InspectionSheet>> {
        return repository.upsertInspectionSheet(vehiculoId, viajeId, viajeTipo, hojaInspeccion)
    }
}