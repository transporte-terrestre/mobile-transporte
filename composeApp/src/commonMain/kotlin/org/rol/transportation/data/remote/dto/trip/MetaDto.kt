package org.rol.transportation.data.remote.dto.trip

import kotlinx.serialization.Serializable

@Serializable
data class MetaDto(
    val total: Int,
    val page: Int,
    val limit: Int,
    val totalPages: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean
)