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

            if (documentId != null) {
                val dto = api.getDocumentInspection(vehiculoId, documentId)
                if (dto != null) {
                    emit(Resource.Success(dto.toDomain()))
                } else {
                    emit(Resource.Success(createEmptyDocumentInspection(vehiculoId)))
                }
            } else {

                emit(Resource.Success(createEmptyDocumentInspection(vehiculoId)))
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

    // --- GENERADOR DE VACÍOS (LOCAL) ---
    private fun createEmptyDocumentInspection(vehiculoId: Int): DocumentInspection {
        // Obtenemos la fecha actual para ponerla por defecto si quieres, o vacía
        val fechaDefault = "" // O Clock.System.now()...

        return DocumentInspection(
            viajeId = null,
            vehiculoId = vehiculoId,
            version = null,
            viajeTipo = null,
            document = DocumentInspectionContent(
                documentosVehiculo = createEmptySection("DOCUMENTOS DEL VEHICULO", listOf(
                    "soatVigente" to "El SOAT se encuentra en la unidad y se encuentra vigente",
                    "tarjetaPropiedad" to "La Tarjeta de Propiedad se encuentra en el vehículo",
                    "tarjetaCirculacion" to "La Tarjeta de Circulación se encuentra en el vehículo y está vigente",
                    "polizaVigente" to "La Póliza se encuentra en el vehículo y se encuentra vigente",
                    "ctivVigente" to "El CTIV se encuentra en el vehículo y se encuentra vigente",
                    "hojasMsdsVehiculo" to "Se cuenta con las Hojas MSDS de los productos químicos",
                    "manualOperacion" to "Se cuenta con el Manual de Operación del Vehículo",
                    "otroVehiculo" to "Otro (Vehículo)"
                ), fechaDefault),
                documentosConductor = createEmptySection("DOCUMENTOS DEL CONDUCTOR", listOf(
                    "licenciaMtc" to "El Conductor cuenta con Licencia del MTC vigente",
                    "licenciaInterna" to "El Conductor cuenta con su Licencia Interna de Manejo vigente",
                    "checkListDiario" to "El Conductor cuenta con el Check List Diario firmado por el Supervisor de Operaciones",
                    "iperc" to "El Conductor cuenta con el IPERC BASE y el Iperc Continúo firmado...",
                    "pets" to "El Conductor cuenta con el PETS correspondiente de su Actividad/Tarea",
                    "hojasMsdsConductor" to "Se cuenta con las Hojas MSDS de los productos químicos (Conductor)",
                    "mapaRiesgos" to "Se cuenta con el mapa de riesgos - Rutas",
                    "otroConductor" to "Otro (Conductor)"
                ), fechaDefault)
            )
        )
    }

    private fun createEmptySection(label: String, items: List<Pair<String, String>>, defaultDate: String): DocumentSection {
        return DocumentSection(
            label = label,
            items = items.associate { (key, label) ->
                key to DocumentItem(
                    label = label,
                    habilitado = false,
                    fechaVencimiento = defaultDate,
                    observacion = ""
                )
            }
        )
    }
}