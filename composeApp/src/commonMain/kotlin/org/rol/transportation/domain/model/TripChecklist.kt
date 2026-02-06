package org.rol.transportation.domain.model

import org.rol.transportation.domain.model.enums.ChecklistType


data class TripChecklist(
    val id: Int?,
    val viajeId: Int,
    val tipo: ChecklistType,
    val items: List<ChecklistItemDetail>,
    val creadoEn: String?,
    val actualizadoEn: String?
)