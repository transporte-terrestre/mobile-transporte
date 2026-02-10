package org.rol.transportation.data.remote.dto.document_inspection

import kotlinx.serialization.Serializable

@Serializable
data class DocumentSectionDto(
    val label: String,
    val items: Map<String, DocumentItemDto>
)
