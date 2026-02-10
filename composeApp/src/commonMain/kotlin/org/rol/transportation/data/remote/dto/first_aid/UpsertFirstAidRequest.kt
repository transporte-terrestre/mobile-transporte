package org.rol.transportation.data.remote.dto.first_aid

import kotlinx.serialization.Serializable

@Serializable
data class UpsertFirstAidRequest(
    val alcohol: UpsertFirstAidItemDto,
    val jabonLiquido: UpsertFirstAidItemDto,
    val gasaEsterilizada: UpsertFirstAidItemDto,
    val apositoEsterilizado: UpsertFirstAidItemDto,
    val esparadrapo: UpsertFirstAidItemDto,
    val vendaElastica: UpsertFirstAidItemDto,
    val banditasAdhesivas: UpsertFirstAidItemDto,
    val tijeraPuntaRoma: UpsertFirstAidItemDto,
    val guantesQuirurgicos: UpsertFirstAidItemDto,
    val algodon: UpsertFirstAidItemDto,
    val maletin: UpsertFirstAidItemDto,
    val ubicacionBotiquin: String
)