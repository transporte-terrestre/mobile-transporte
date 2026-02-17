package org.rol.transportation.data.remote.dto.trip

import kotlinx.serialization.Serializable
import org.rol.transportation.data.remote.dto.route.RouteDto

@Serializable
data class TripDto (
    val id: Int,
    val rutaId: Int? = null,
    val rutaOcasional: String? = null,
    val distanciaEstimada: String,
    val distanciaFinal: String? = null,
    val tipoRuta: String,
    val clienteId: Int,
    val tripulantes: List<String> = emptyList(),
    val modalidadServicio: String,
    val horasContrato: String,
    val estado: String,
    val fechaSalida: String,
    val fechaLlegada: String? = null,
    val creadoEn: String,
    val actualizadoEn: String,
    val eliminadoEn: String? = null,
    val cliente: CustomerDto,
    val ruta: RouteDto? = null,
    val conductores: List<DriverDto> = emptyList(),
    val vehiculos: List<VehicleDto> = emptyList(),
    val comentarios: List<CommentDto> = emptyList(),
    val checkInSalida: Boolean? = false,
    val checkInLlegada: Boolean? = false
)