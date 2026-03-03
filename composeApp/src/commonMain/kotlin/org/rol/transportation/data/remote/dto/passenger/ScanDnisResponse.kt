package org.rol.transportation.data.remote.dto.passenger

import kotlinx.serialization.Serializable

@Serializable
data class ScanDnisResponse(
    val exito: Boolean,
    val mensaje: String,
    val resultados: List<ScanResultDto>,
    val pasajerosActualizados: List<PassengerAttendanceDto>
)

@Serializable
data class ScanResultDto(
    val dni: String? = null,
    val nombres: String? = null,
    val apellidos: String? = null,
    val matched: Boolean,
    val status: String? = null,
    val viajeTramoId: Int? = null
)
