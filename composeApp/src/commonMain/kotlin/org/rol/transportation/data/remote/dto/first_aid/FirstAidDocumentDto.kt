package org.rol.transportation.data.remote.dto.first_aid

import kotlinx.serialization.Serializable

@Serializable
data class FirstAidDocumentDto(
    val alcohol: FirstAidItemDto?,
    val jabonLiquido: FirstAidItemDto?,
    val gasaEsterilizada: FirstAidItemDto?,
    val apositoEsterilizado: FirstAidItemDto?,
    val esparadrapo: FirstAidItemDto?,
    val vendaElastica: FirstAidItemDto?,
    val banditasAdhesivas: FirstAidItemDto?,
    val tijeraPuntaRoma: FirstAidItemDto?,
    val guantesQuirurgicos: FirstAidItemDto?,
    val algodon: FirstAidItemDto?,
    val maletin: FirstAidItemDto?,
    val ubicacionBotiquin: String?
)