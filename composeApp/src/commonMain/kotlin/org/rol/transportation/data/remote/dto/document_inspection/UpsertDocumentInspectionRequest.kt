package org.rol.transportation.data.remote.dto.document_inspection

import kotlinx.serialization.Serializable

@Serializable
data class UpsertDocumentInspectionRequest(
    val soatVigente: UpsertDocumentItemDto,
    val tarjetaPropiedad: UpsertDocumentItemDto,
    val tarjetaCirculacion: UpsertDocumentItemDto,
    val polizaVigente: UpsertDocumentItemDto,
    val ctivVigente: UpsertDocumentItemDto,
    val hojasMsdsVehiculo: UpsertDocumentItemDto,
    val manualOperacion: UpsertDocumentItemDto,
    val otroVehiculo: UpsertDocumentItemDto,
    val licenciaMtc: UpsertDocumentItemDto,
    val licenciaInterna: UpsertDocumentItemDto,
    val checkListDiario: UpsertDocumentItemDto,
    val iperc: UpsertDocumentItemDto,
    val pets: UpsertDocumentItemDto,
    val hojasMsdsConductor: UpsertDocumentItemDto,
    val mapaRiesgos: UpsertDocumentItemDto,
    val otroConductor: UpsertDocumentItemDto
)
