package org.rol.transportation.data.remote.dto.check_document

import kotlinx.serialization.Serializable

@Serializable
data class UpsertChecklistDocumentRequest(
    val photo: PhotoDto
)