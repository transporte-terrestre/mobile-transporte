package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.rol.transportation.data.remote.api.FirstAidApi
import org.rol.transportation.data.remote.dto.first_aid.FirstAidDto
import org.rol.transportation.data.remote.dto.first_aid.FirstAidItemDto
import org.rol.transportation.data.remote.dto.first_aid.UpsertFirstAidItemDto
import org.rol.transportation.data.remote.dto.first_aid.UpsertFirstAidRequest
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.first_aid.FirstAidInspection
import org.rol.transportation.domain.model.first_aid.FirstAidItem
import org.rol.transportation.utils.Resource


class FirstAidRepositoryImpl(private val api: FirstAidApi) : FirstAidRepository {

    override suspend fun getFirstAid(vehiculoId: Int, documentId: Int?): Flow<Resource<FirstAidInspection>> = flow {
        try {
            emit(Resource.Loading)
            if (documentId != null) {
                val dto = api.getFirstAid(vehiculoId, documentId)
                if (dto != null) emit(Resource.Success(dto.toDomain()))
                else emit(Resource.Success(createEmptyLocal(vehiculoId)))
            } else {
                emit(Resource.Success(createEmptyLocal(vehiculoId)))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener botiquín"))
        }
    }

    override suspend fun upsertFirstAid(
        vehiculoId: Int,
        viajeId: Int,
        viajeTipo: TripType,
        inspection: FirstAidInspection
    ): Flow<Resource<FirstAidInspection>> = flow {
        try {
            emit(Resource.Loading)
            val request = inspection.toUpsertRequest()
            val response = api.upsertFirstAid(vehiculoId, viajeId, viajeTipo.value, request)
            emit(Resource.Success(response.toDomain()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al guardar botiquín"))
        }
    }

    // --- MAPPERS ---
    private fun FirstAidDto.toDomain(): FirstAidInspection {
        val doc = document
        // Construimos el mapa manualmente para asegurar las llaves
        val items = mapOf(
            "alcohol" to doc.alcohol, "jabonLiquido" to doc.jabonLiquido,
            "gasaEsterilizada" to doc.gasaEsterilizada, "apositoEsterilizado" to doc.apositoEsterilizado,
            "esparadrapo" to doc.esparadrapo, "vendaElastica" to doc.vendaElastica,
            "banditasAdhesivas" to doc.banditasAdhesivas, "tijeraPuntaRoma" to doc.tijeraPuntaRoma,
            "guantesQuirurgicos" to doc.guantesQuirurgicos, "algodon" to doc.algodon,
            "maletin" to doc.maletin
        ).mapValues { it.value?.toDomain() ?: FirstAidItem("Error", false, "", "", "") }

        return FirstAidInspection(
            viajeId = viajeId, vehiculoId = vehiculoId, version = version, viajeTipo = viajeTipo,
            location = doc.ubicacionBotiquin ?: "",
            items = items
        )
    }

    private fun FirstAidItemDto.toDomain() = FirstAidItem(
        label = label, habilitado = habilitado,
        fechaVencimiento = fechaVencimiento ?: "",
        fechaSalida = fechaSalida ?: "",
        fechaReposicion = fechaReposicion ?: ""
    )

    private fun FirstAidInspection.toUpsertRequest(): UpsertFirstAidRequest {
        fun map(key: String): UpsertFirstAidItemDto {
            val item = items[key]
            return UpsertFirstAidItemDto(
                habilitado = item?.habilitado ?: false,
                fechaVencimiento = item?.fechaVencimiento?.ifBlank { null },
                fechaSalida = item?.fechaSalida?.ifBlank { null },
                fechaReposicion = item?.fechaReposicion?.ifBlank { null }
            )
        }

        return UpsertFirstAidRequest(
            alcohol = map("alcohol"), jabonLiquido = map("jabonLiquido"),
            gasaEsterilizada = map("gasaEsterilizada"), apositoEsterilizado = map("apositoEsterilizado"),
            esparadrapo = map("esparadrapo"), vendaElastica = map("vendaElastica"),
            banditasAdhesivas = map("banditasAdhesivas"), tijeraPuntaRoma = map("tijeraPuntaRoma"),
            guantesQuirurgicos = map("guantesQuirurgicos"), algodon = map("algodon"),
            maletin = map("maletin"),
            ubicacionBotiquin = location
        )
    }

    private fun createEmptyLocal(vehiculoId: Int): FirstAidInspection {
        val labels = mapOf(
            "alcohol" to "Alcohol de 70º 500ml", "jabonLiquido" to "Jabón líquido antiséptico 400ml",
            "gasaEsterilizada" to "Gasa esterilizada fraccionadas 10 x 10cm",
            "apositoEsterilizado" to "Apósito esterilizado 10x10cm",
            "esparadrapo" to "Esparadrapo 2.5cm x 5m", "vendaElastica" to "Venda elástica 4\"x5yardas",
            "banditasAdhesivas" to "Banditas adhesivas (curitas)", "tijeraPuntaRoma" to "Tijera punta roma 3\"",
            "guantesQuirurgicos" to "Guantes quirúrgicos esterilizados 7 ½",
            "algodon" to "Algodón 50 gr", "maletin" to "Maletín"
        )

        val items = labels.mapValues { (_, label) ->
            FirstAidItem(label, false, "", "", "")
        }

        return FirstAidInspection(null, vehiculoId, null, null, "", items)
    }
}