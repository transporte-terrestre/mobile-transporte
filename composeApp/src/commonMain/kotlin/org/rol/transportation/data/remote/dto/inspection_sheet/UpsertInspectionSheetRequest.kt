package org.rol.transportation.data.remote.dto.inspection_sheet

import kotlinx.serialization.Serializable

@Serializable
data class UpsertInspectionSheetRequest (
    // Declaración Jurada
    val descansoSuficiente: Boolean,
    val condicionesFisicas: Boolean,
    val medicamentos: Boolean,
    val condicionesEmocionales: Boolean,
    val conscienteResponsabilidad: Boolean,

    // Estado General
    val farosPrincipales: Boolean,
    val lucesDireccionales: Boolean,
    val lucesFreno: Boolean,
    val luzEstroboscopica: Boolean,
    val espejos: Boolean,
    val parabrisasVentanas: Boolean,
    val plumillas: Boolean,
    val neumaticos: Boolean,
    val esparragosTuercas: Boolean,
    val cartolas: Boolean,

    // Estado Interno
    val cinturonesEstado: Boolean,
    val alarmaRetroceso: Boolean,
    val segurosPuertas: Boolean,
    val claxon: Boolean,
    val documentosVigentes: Boolean,
    val kitHerramientas: Boolean,
    val gata: Boolean,
    val ordenLimpieza: Boolean,
    val jaulaAntivuelco: Boolean,
    val aireAcondicionado: Boolean,

    // Elementos de Seguridad
    val tacosCunas: Boolean,
    val conosSeguridad: Boolean,
    val eslingaGrilletes: Boolean,
    val picoPala: Boolean,
    val cableCorriente: Boolean,
    val extintor: Boolean,
    val botiquinLinterna: Boolean,
    val kitAntiderrame: Boolean,
    val sistemaComunicacion: Boolean,

    // Estado Mecánico
    val pruebaFrenoServicio: Boolean,
    val frenoEstacionamiento: Boolean,
    val direccion: Boolean,
    val nivelAceiteMotor: Boolean,
    val nivelLiquidoFrenos: Boolean,
    val nivelRefrigerante: Boolean,
    val nivelAceiteHidraulico: Boolean,
    val nivelAguaLimpiaparabrisas: Boolean,
    val otrosMecanico: Boolean,

    // Sistemas Críticos
    val mobiliEye: Boolean,
    val sensorFatiga: Boolean,
    val frenosABS: Boolean,
    val espEsc: Boolean,

    // Cinturones de Seguridad
    val cinturonesPilotos: Boolean,
    val cinturonesPasajes: Boolean
)