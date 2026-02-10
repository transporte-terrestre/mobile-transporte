package org.rol.transportation.data.remote.dto.lights_alarm

import kotlinx.serialization.Serializable

@Serializable
data class UpsertLightsAlarmRequest(
    val alarmaRetroceso: UpsertLightItemDto,
    val alarmaCinturon: UpsertLightItemDto,
    val claxon: UpsertLightItemDto,
    val lucesCabina: UpsertLightItemDto,
    val lucesSalon: UpsertLightItemDto,
    val lucesAltasDerecho: UpsertLightItemDto,
    val lucesAltasIzquierdo: UpsertLightItemDto,
    val lucesBajasDerecho: UpsertLightItemDto,
    val lucesBajasIzquierdo: UpsertLightItemDto,
    val lucesLateralesDerecho: UpsertLightItemDto,
    val lucesLateralesIzquierdo: UpsertLightItemDto,
    val lucesNeblineros: UpsertLightItemDto,
    val lucesEstacionamientoDerecho: UpsertLightItemDto,
    val lucesEstacionamientoIzquierdo: UpsertLightItemDto,
    val lucesDireccionalesDerecho: UpsertLightItemDto,
    val lucesDireccionalesIzquierdo: UpsertLightItemDto,
    val luzEstroboscopica: UpsertLightItemDto,
    val luzPertiga: UpsertLightItemDto,
    val pruebaRadio: UpsertLightItemDto,
    val botonPanico: UpsertLightItemDto
)
