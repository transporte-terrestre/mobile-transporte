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

    override suspend fun getTools(vehiculoId: Int, documentId: Int?): Flow<Resource<ToolsInspection>> = flow {
        try {
            emit(Resource.Loading)
            if (documentId != null) {
                val dto = api.getTools(vehiculoId, documentId)
                if (dto != null) emit(Resource.Success(dto.toDomain()))
                else emit(Resource.Success(createEmptyLocal(vehiculoId)))
            } else {
                emit(Resource.Success(createEmptyLocal(vehiculoId)))
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

    // --- EMPTY LOCAL ---
    private fun createEmptyLocal(vehiculoId: Int): ToolsInspection {
        // Definimos las leyendas locales por si es la primera vez y no hay internet para traer "info"
        val criteria = mapOf(
            "criterioA" to "HERRAMIENTA SIN GRASA IMPREGNADA",
            "criterioB" to "EMPALME Y CONECCIONES",
            "criterioC" to "ALMACENAMIENTO ADECUADO",
            "criterioD" to "GOLPES Y ABOLLADURAS",
            "criterioE" to "LIMPIA Y ORDENADA",
            "criterioF" to "OTRO"
        )

        // Lista de herramientas basada en tu curl
        val labels = mapOf(
            "llavesMixtas" to "Llaves Mixtas",
            "destornilladorEstrella" to "Destornillador Estrella",
            "destornilladorPlano" to "Destornillador Plano",
            "alicate" to "Alicate",
            "llaveRuedaPalanca" to "Llave de Rueda con Palanca",
            "trianguloSeguridad" to "Triangulo de Seguridad",
            "conosPeligro" to "Conos de Peligro",
            "cableCorriente" to "Cable para Corriente",
            "eslinga" to "Eslinga",
            "grilletes" to "Grilletes",
            "tacosCunas" to "Tacos Cuñas",
            "linterna" to "Linterna",
            "extintor" to "Extintor",
            "pico" to "Pico",
            "medidorAire" to "Medidor de Aire",
            "varasLuminosas" to "Varas Luminosas Rojo y Verde",
            "paletaPareSiga" to "Paleta Pare y Siga",
            "escoba" to "Escoba",
            "balde" to "Balde",
            "cuadernoBitacora" to "Cuaderno Bitacora",
            "gataHidraulica" to "Gata Hidraulica",
            "cajaHerramientas" to "Caja o Maleta de Herramienta"
        )

        val items = labels.mapValues { (_, label) ->
            ToolItem(
                label = label,
                estado = false, stock = "",
                criterioA = true, criterioB = true, criterioC = true, // Default true según tu curl
                criterioD = true, criterioE = true, criterioF = true,
                accionCorrectiva = "", observacion = ""
            )
        }

        return ToolsInspection(
            viajeId = null, vehiculoId = vehiculoId, version = null, viajeTipo = null,
            criteriaLabels = criteria, items = items
        )
    }
}