package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.rol.transportation.data.remote.api.DocumentInspectionApi
import org.rol.transportation.data.remote.dto.document_inspection.DocumentInspectionDto
import org.rol.transportation.data.remote.dto.document_inspection.DocumentItemDto
import org.rol.transportation.data.remote.dto.document_inspection.DocumentSectionDto
import org.rol.transportation.data.remote.dto.document_inspection.UpsertDocumentInspectionRequest
import org.rol.transportation.data.remote.dto.document_inspection.UpsertDocumentItemDto
import org.rol.transportation.domain.model.document_inspection.DocumentInspection
import org.rol.transportation.domain.model.document_inspection.DocumentInspectionContent
import org.rol.transportation.domain.model.document_inspection.DocumentItem
import org.rol.transportation.domain.model.document_inspection.DocumentSection
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.utils.Resource

class DocumentInspectionRepositoryImpl(
    private val api: DocumentInspectionApi
) : DocumentInspectionRepository {

    override suspend fun getDocumentInspection(
        vehiculoId: Int,
        documentId: Int?
    ): Flow<Resource<DocumentInspection>> = flow {
        try {
            emit(Resource.Loading)

            val dto = api.getDocumentInspection(vehiculoId, documentId)

            if (dto != null) {
                emit(Resource.Success(dto.toDomain()))
            } else {
                emit(Resource.Error("El servidor no devolvió la estructura de documentos."))
            }

        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener documentos"))
        }
    }

    override suspend fun upsertDocumentInspection(
        vehiculoId: Int,
        viajeId: Int,
        viajeTipo: TripType,
        inspection: DocumentInspection
    ): Flow<Resource<DocumentInspection>> = flow {
        try {
            emit(Resource.Loading)
            val request = inspection.toUpsertRequest()
            val response = api.upsertDocumentInspection(
                vehiculoId,
                viajeId,
                viajeTipo.value,
                request
            )
            emit(Resource.Success(response.toDomain()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al guardar documentos"))
        }
    }

    // --- MAPPERS ---

    // Mapeo manual para asegurar que la estructura plana del POST se llene correctamente
    private fun DocumentInspection.toUpsertRequest(): UpsertDocumentInspectionRequest {
        val vehiculo = document.documentosVehiculo.items
        val conductor = document.documentosConductor.items

        // Función auxiliar corregida: Mapea a UpsertDocumentItemDto (SIN LABEL)
        fun mapItem(item: DocumentItem?): UpsertDocumentItemDto {
            val fechaEnvio = if (item?.fechaVencimiento.isNullOrBlank()) null else item.fechaVencimiento

            return UpsertDocumentItemDto(
                habilitado = item?.habilitado ?: false,
                fechaVencimiento = fechaEnvio,
                observacion = item?.observacion
            )
        }

        return UpsertDocumentInspectionRequest(
            soatVigente = mapItem(vehiculo["soatVigente"]),
            tarjetaPropiedad = mapItem(vehiculo["tarjetaPropiedad"]),
            tarjetaCirculacion = mapItem(vehiculo["tarjetaCirculacion"]),
            polizaVigente = mapItem(vehiculo["polizaVigente"]),
            ctivVigente = mapItem(vehiculo["ctivVigente"]),
            hojasMsdsVehiculo = mapItem(vehiculo["hojasMsdsVehiculo"]),
            manualOperacion = mapItem(vehiculo["manualOperacion"]),
            otroVehiculo = mapItem(vehiculo["otroVehiculo"]),

            licenciaMtc = mapItem(conductor["licenciaMtc"]),
            licenciaInterna = mapItem(conductor["licenciaInterna"]),
            checkListDiario = mapItem(conductor["checkListDiario"]),
            iperc = mapItem(conductor["iperc"]),
            pets = mapItem(conductor["pets"]),
            hojasMsdsConductor = mapItem(conductor["hojasMsdsConductor"]),
            mapaRiesgos = mapItem(conductor["mapaRiesgos"]),
            otroConductor = mapItem(conductor["otroConductor"])
        )
    }

    private fun DocumentInspectionDto.toDomain() = DocumentInspection(
        viajeId = viajeId,
        vehiculoId = vehiculoId,
        version = version,
        viajeTipo = viajeTipo,
        document = DocumentInspectionContent(
            documentosVehiculo = document.documentosVehiculo.toDomain(),
            documentosConductor = document.documentosConductor.toDomain()
        )
    )

    private fun DocumentSectionDto.toDomain() = DocumentSection(
        label = label,
        items = items.mapValues { it.value.toDomain() }
    )

    private fun DocumentItemDto.toDomain() = DocumentItem(
        label = label,
        habilitado = habilitado,
        fechaVencimiento = fechaVencimiento ?: "",
        observacion = observacion ?: ""
    )

}