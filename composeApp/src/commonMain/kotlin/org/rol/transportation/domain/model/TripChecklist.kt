package org.rol.transportation.domain.model

import org.rol.transportation.domain.model.enums.ChecklistType

data class TripChecklist(
    val id: Int,
    val viajeId: Int,
    val tipo: ChecklistType,
    val validadoEn: String,
    val observaciones: String?,
    val items: List<ChecklistItemDetail>
)