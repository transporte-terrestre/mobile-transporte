package org.rol.transportation.data.remote.dto.document_inspection

import kotlinx.serialization.Serializable

@Serializable
data class DocumentItemDto(
    val label: String,
    val habilitado: Boolean,
    val fechaVencimiento: String?,
    val observacion: String?
)