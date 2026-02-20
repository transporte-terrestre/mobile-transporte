package org.rol.transportation.domain.model

import org.rol.transportation.domain.model.enums.TripStatus

data class TripSegment(
    val id: Int,
    val cliente: Customer?,
    val ruta: Route?,
    val rutaOcasional: String?,
    val conductorPrincipal: Driver?,
    val vehiculoPrincipal: Vehicle?,
    val estado: TripStatus,
    val fechaSalida: String,
    val fechaLlegada: String?,
    val distanciaEstimada: String?,
    val distanciaFinal: String?,
    val modalidadServicio: String?,
    val turno: String?,
    val sentido: String?,
    val checkInSalida: Boolean = false,
    val checkInLlegada: Boolean = false
)