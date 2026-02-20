package org.rol.transportation.data.remote.dto.trip

import kotlinx.serialization.Serializable
import org.rol.transportation.data.remote.dto.trip.MetaDto


@Serializable
data class TripLightDto(
    val id: Int,
    val creadoEn: String? = null,
    val actualizadoEn: String? = null,
    val eliminadoEn: String? = null,
    val ida: TripLightSegmentDto,
    val vuelta: TripLightSegmentDto? = null
)


