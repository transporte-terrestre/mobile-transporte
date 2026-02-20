package org.rol.transportation.data.remote.dto.trip

import kotlinx.serialization.Serializable
import org.rol.transportation.data.remote.dto.route.RouteDto

@Serializable
data class TripSegmentDto (
    val id: Int,
    val rutaId: Int? = null,
    val rutaOcasional: String? = null,
    val distanciaEstimada: String? = null,
    val distanciaFinal: String? = null,
    val tipoRuta: String? = null,
    val clienteId: Int? = null,
    val modalidadServicio: String? = null,
    val horasContrato: String? = null,
    val estado: String? = null,
    val turno: String? = null,
    val sentido: String? = null,
    val numeroVale: String? = null,
    val fechaSalida: String? = null,
    val fechaLlegada: String? = null,
    val vehiculoPrincipal: VehicleDto? = null,
    val conductorPrincipal: DriverDto? = null,
    val vehiculos: List<VehicleDto>? = null,
    val conductores: List<DriverDto>? = null,
    val ruta: RouteDto? = null,
    val cliente: CustomerDto? = null,
    val checkInSalida: Boolean? = false,
    val checkInLlegada: Boolean? = false
)
