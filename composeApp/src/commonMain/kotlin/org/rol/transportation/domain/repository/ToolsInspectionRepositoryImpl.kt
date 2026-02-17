package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.rol.transportation.data.remote.api.ToolsInspectionApi
import org.rol.transportation.data.remote.dto.tools_inspection.ToolItemDto
import org.rol.transportation.data.remote.dto.tools_inspection.ToolsInspectionDto
import org.rol.transportation.data.remote.dto.tools_inspection.UpsertToolItemDto
import org.rol.transportation.data.remote.dto.tools_inspection.UpsertToolsRequest
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.tools_inspection.ToolItem
import org.rol.transportation.domain.model.tools_inspection.ToolsInspection
import org.rol.transportation.utils.Resource

class ToolsInspectionRepositoryImpl(private val api: ToolsInspectionApi) : ToolsInspectionRepository {

    override suspend fun getTools(
        vehiculoId: Int,
        documentId: Int?
    ): Flow<Resource<ToolsInspection>> = flow {
        try {
            emit(Resource.Loading)

            val dto = api.getTools(vehiculoId, documentId)

            if (dto != null) {
                emit(Resource.Success(dto.toDomain()))
            } else {
                emit(Resource.Error("El servidor no devolvió la estructura de herramientas."))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener herramientas"))
        }
    }

    override suspend fun upsertTools(
        vehiculoId: Int,
        viajeId: Int,
        viajeTipo: TripType,
        inspection: ToolsInspection
    ): Flow<Resource<ToolsInspection>> = flow {
        try {
            emit(Resource.Loading)
            val request = inspection.toUpsertRequest()
            val response = api.upsertTools(vehiculoId, viajeId, viajeTipo.value, request)
            emit(Resource.Success(response.toDomain()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al guardar herramientas"))
        }
    }

    // --- MAPPERS ---
    private fun ToolsInspectionDto.toDomain() = ToolsInspection(
        viajeId = viajeId,
        vehiculoId = vehiculoId,
        version = version,
        viajeTipo = viajeTipo,
        criteriaLabels = document.info ?: emptyMap(),
        items = document.herramientas.mapValues { it.value.toDomain() }
    )

    private fun ToolItemDto.toDomain() = ToolItem(
        label = label,
        estado = estado,
        stock = stock ?: "",
        criterioA = criterioA, criterioB = criterioB, criterioC = criterioC,
        criterioD = criterioD, criterioE = criterioE, criterioF = criterioF,
        accionCorrectiva = accionCorrectiva ?: "",
        observacion = observacion ?: ""
    )

    private fun ToolsInspection.toUpsertRequest(): UpsertToolsRequest {
        return items.mapValues { (_, item) ->
            UpsertToolItemDto(
                estado = item.estado,
                stock = item.stock.ifBlank { null },
                criterioA = item.criterioA, criterioB = item.criterioB, criterioC = item.criterioC,
                criterioD = item.criterioD, criterioE = item.criterioE, criterioF = item.criterioF,
                accionCorrectiva = item.accionCorrectiva.ifBlank { null },
                observacion = item.observacion.ifBlank { null }
            )
        }
    }

}