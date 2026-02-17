package org.rol.transportation.data.remote.dto.check

import kotlinx.serialization.Serializable

@Serializable
data class ChecklistItemDetailDto(
    val checklistItemId: Int,
    val vehiculoChecklistDocumentId: Int? = null,
    val nombre: String,
    val descripcion: String,
    val orden: Int,
    val observacion: String? = null,
    val creadoEn: String? = null,
    val actualizadoEn: String? = null,
    val isUpdate: Boolean

)