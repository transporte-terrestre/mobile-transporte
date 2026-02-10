package org.rol.transportation.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.rol.transportation.data.remote.api.InspectionSheetApi
import org.rol.transportation.data.remote.dto.inspection_sheet.InspectionItemDto
import org.rol.transportation.data.remote.dto.inspection_sheet.InspectionSectionDto
import org.rol.transportation.data.remote.dto.inspection_sheet.InspectionSheetDto
import org.rol.transportation.data.remote.dto.inspection_sheet.UpsertInspectionSheetRequest
import org.rol.transportation.domain.model.enums.ChecklistDocumentType
import org.rol.transportation.domain.model.enums.TripType
import org.rol.transportation.domain.model.inspection_sheet.InspectionItem
import org.rol.transportation.domain.model.inspection_sheet.InspectionSection
import org.rol.transportation.domain.model.inspection_sheet.InspectionSheet
import org.rol.transportation.domain.model.inspection_sheet.InspectionSheetDocument
import org.rol.transportation.utils.Resource

class InspectionSheetRepositoryImpl (
    private val inspectionSheetApi: InspectionSheetApi
) : InspectionSheetRepository {

    override suspend fun getInspectionSheet(
        vehiculoId: Int,
        documentId: Int?
    ): Flow<Resource<InspectionSheet>> = flow {
        try {
            emit(Resource.Loading)
            if (documentId != null) {
                val hojaDto = inspectionSheetApi.getInspectionSheet(vehiculoId, documentId)

                if (hojaDto != null) {
                    emit(Resource.Success(hojaDto.toDomain()))
                } else {
                    // Si por alguna razón el ID existe pero no devuelve data (404), damos una vacía
                    emit(Resource.Success(createEmptyInspectionSheet(vehiculoId)))
                }
            } else {
                emit(Resource.Success(createEmptyInspectionSheet(vehiculoId)))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener la hoja de inspección"))
        }
    }

    override suspend fun upsertInspectionSheet(
        vehiculoId: Int,
        viajeId: Int,
        viajeTipo: TripType,
        hojaInspeccion: InspectionSheet
    ): Flow<Resource<InspectionSheet>> = flow {
        try {
            emit(Resource.Loading)
            val request = hojaInspeccion.toUpsertRequest()
            val response = inspectionSheetApi.upsertInspectionSheet(
                vehiculoId,
                viajeId,
                viajeTipo.value,
                request
            )
            emit(Resource.Success(response.toDomain()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al guardar la hoja de inspección"))
        }
    }


    // FUNCIONES DE MAPPING PRIVADAS (igual que en ChecklistDocumentRepositoryImpl)
    private fun InspectionSheetDto.toDomain() = InspectionSheet(
        viajeId = viajeId,
        vehiculoId = vehiculoId,
        version = version,
        viajeTipo = viajeTipo,
        document = InspectionSheetDocument(
            declaracionJurada = document.declaracionJurada.toDomain(),
            estadoGeneral = document.estadoGeneral.toDomain(),
            estadoInterno = document.estadoInterno.toDomain(),
            elementosSeguridad = document.elementosSeguridad.toDomain(),
            estadoMecanico = document.estadoMecanico.toDomain(),
            sistemasCriticos = document.sistemasCriticos.toDomain(),
            cinturonesSeguridad = document.cinturonesSeguridad.toDomain()
        )
    )

    private fun InspectionSectionDto.toDomain() = InspectionSection(
        label = label,
        items = items.mapValues { it.value.toDomain() }
    )

    private fun InspectionItemDto.toDomain() = InspectionItem(
        label = label,
        color = color,
        value = value
    )

    private fun InspectionSheet.toUpsertRequest() = UpsertInspectionSheetRequest(
        // Declaración Jurada
        descansoSuficiente = document.declaracionJurada.items["descansoSuficiente"]?.value ?: false,
        condicionesFisicas = document.declaracionJurada.items["condicionesFisicas"]?.value ?: false,
        medicamentos = document.declaracionJurada.items["medicamentos"]?.value ?: false,
        condicionesEmocionales = document.declaracionJurada.items["condicionesEmocionales"]?.value
            ?: false,
        conscienteResponsabilidad = document.declaracionJurada.items["conscienteResponsabilidad"]?.value
            ?: false,

        // Estado General
        farosPrincipales = document.estadoGeneral.items["farosPrincipales"]?.value ?: false,
        lucesDireccionales = document.estadoGeneral.items["lucesDireccionales"]?.value ?: false,
        lucesFreno = document.estadoGeneral.items["lucesFreno"]?.value ?: false,
        luzEstroboscopica = document.estadoGeneral.items["luzEstroboscopica"]?.value ?: false,
        espejos = document.estadoGeneral.items["espejos"]?.value ?: false,
        parabrisasVentanas = document.estadoGeneral.items["parabrisasVentanas"]?.value ?: false,
        plumillas = document.estadoGeneral.items["plumillas"]?.value ?: false,
        neumaticos = document.estadoGeneral.items["neumaticos"]?.value ?: false,
        esparragosTuercas = document.estadoGeneral.items["esparragosTuercas"]?.value ?: false,
        cartolas = document.estadoGeneral.items["cartolas"]?.value ?: false,

        // Estado Interno
        cinturonesEstado = document.estadoInterno.items["cinturonesEstado"]?.value ?: false,
        alarmaRetroceso = document.estadoInterno.items["alarmaRetroceso"]?.value ?: false,
        segurosPuertas = document.estadoInterno.items["segurosPuertas"]?.value ?: false,
        claxon = document.estadoInterno.items["claxon"]?.value ?: false,
        documentosVigentes = document.estadoInterno.items["documentosVigentes"]?.value ?: false,
        kitHerramientas = document.estadoInterno.items["kitHerramientas"]?.value ?: false,
        gata = document.estadoInterno.items["gata"]?.value ?: false,
        ordenLimpieza = document.estadoInterno.items["ordenLimpieza"]?.value ?: false,
        jaulaAntivuelco = document.estadoInterno.items["jaulaAntivuelco"]?.value ?: false,
        aireAcondicionado = document.estadoInterno.items["aireAcondicionado"]?.value ?: false,

        // Elementos de Seguridad
        tacosCunas = document.elementosSeguridad.items["tacosCunas"]?.value ?: false,
        conosSeguridad = document.elementosSeguridad.items["conosSeguridad"]?.value ?: false,
        eslingaGrilletes = document.elementosSeguridad.items["eslingaGrilletes"]?.value ?: false,
        picoPala = document.elementosSeguridad.items["picoPala"]?.value ?: false,
        cableCorriente = document.elementosSeguridad.items["cableCorriente"]?.value ?: false,
        extintor = document.elementosSeguridad.items["extintor"]?.value ?: false,
        botiquinLinterna = document.elementosSeguridad.items["botiquinLinterna"]?.value ?: false,
        kitAntiderrame = document.elementosSeguridad.items["kitAntiderrame"]?.value ?: false,
        sistemaComunicacion = document.elementosSeguridad.items["sistemaComunicacion"]?.value
            ?: false,

        // Estado Mecánico
        pruebaFrenoServicio = document.estadoMecanico.items["pruebaFrenoServicio"]?.value ?: false,
        frenoEstacionamiento = document.estadoMecanico.items["frenoEstacionamiento"]?.value
            ?: false,
        direccion = document.estadoMecanico.items["direccion"]?.value ?: false,
        nivelAceiteMotor = document.estadoMecanico.items["nivelAceiteMotor"]?.value ?: false,
        nivelLiquidoFrenos = document.estadoMecanico.items["nivelLiquidoFrenos"]?.value ?: false,
        nivelRefrigerante = document.estadoMecanico.items["nivelRefrigerante"]?.value ?: false,
        nivelAceiteHidraulico = document.estadoMecanico.items["nivelAceiteHidraulico"]?.value
            ?: false,
        nivelAguaLimpiaparabrisas = document.estadoMecanico.items["nivelAguaLimpiaparabrisas"]?.value
            ?: false,
        otrosMecanico = document.estadoMecanico.items["otrosMecanico"]?.value ?: false,

        // Sistemas Críticos
        mobiliEye = document.sistemasCriticos.items["mobiliEye"]?.value ?: false,
        sensorFatiga = document.sistemasCriticos.items["sensorFatiga"]?.value ?: false,
        frenosABS = document.sistemasCriticos.items["frenosABS"]?.value ?: false,
        espEsc = document.sistemasCriticos.items["espEsc"]?.value ?: false,

        // Cinturones de Seguridad
        cinturonesPilotos = document.cinturonesSeguridad.items["cinturonesPilotos"]?.value ?: false,
        cinturonesPasajes = document.cinturonesSeguridad.items["cinturonesPasajes"]?.value ?: false
    )


    private fun createEmptyInspectionSheet(vehiculoId: Int): InspectionSheet {
        return InspectionSheet(
            viajeId = null,
            vehiculoId = vehiculoId,
            version = null,
            viajeTipo = null,
            document = InspectionSheetDocument(
                declaracionJurada = createEmptySection(
                    "DECLARACION JURADA DEL CONDUCTOR", listOf(
                        "descansoSuficiente" to "He descansado lo suficiente y me encuentro en condiciones de conducir.",
                        "condicionesFisicas" to "Me siento en buenas condiciones fisicas y no tengo ninguna dolencia o enfermedad que me impida conducir en forma segura.",
                        "medicamentos" to "Estoy tomando medicamentos recetados por un médico quien me ha asegurado que no son impedimento para conducir de forma segura.",
                        "condicionesEmocionales" to "Me encuentro emocional y personalmente en buenas condiciones para poder concentrarme en la conducción segura del vehículo.",
                        "conscienteResponsabilidad" to "Estoy consciente de la responsabilidad que significa conducir este vehículo, sin poner en riesgo mi integridad, la de mis compañeros ni el patrimonio de la empresa ."
                    ), "rojo"
                ),
                estadoGeneral = createEmptySection(
                    "ESTADO GENERAL (vuelta del gallo)", listOf(
                        "farosPrincipales" to "Faros principales / Faros neblineros / Luces pirata",
                        "lucesDireccionales" to "Luces direccionales / de estacionamiento / intermitentes",
                        "lucesFreno" to "Luces de freno/ Luces de retroceso",
                        "luzEstroboscopica" to "Luz estroboscópica(Circulina color verde)",
                        "espejos" to "Espejos",
                        "parabrisasVentanas" to "Parabrisa y ventanas (sin rajaduras)",
                        "plumillas" to "Plumillas Limpia y lava parabrisas",
                        "neumaticos" to "Neumáticos(Incluye repuesto ) tipo AT(Presión, banda de rodamiento)",
                        "esparragosTuercas" to "Esparragos, tuercas(torque según fabricante), seguro de tuercas",
                        "cartolas" to "Cartolas /Letreros Identificatorios"
                    ), "amarillo"
                ),
                estadoInterno = createEmptySection(
                    "Estado interno", listOf(
                        "cinturonesEstado" to "Cinturones de seguridad / Estado",
                        "alarmaRetroceso" to "Alarma de Retroceso",
                        "segurosPuertas" to "Seguros de puertas",
                        "claxon" to "Claxon",
                        "documentosVigentes" to "Tarjeta de propiedad, SOAT,Revisión Técnica vigente / ATS",
                        "kitHerramientas" to "Kit básico de herramientas * / Llave de rueda tipo cruz",
                        "gata" to "Gata(Doble peso bruto vehículo) y sus accesorios",
                        "ordenLimpieza" to "Orden y limpieza",
                        "jaulaAntivuelco" to "Jaula Interna/Externa Antivuelco",
                        "aireAcondicionado" to "Aire Acondicionado"
                    ), "verde"
                ),
                elementosSeguridad = createEmptySection(
                    "Elementos de seguridad", listOf(
                        "tacosCunas" to "Tacos/cuñas (02)",
                        "conosSeguridad" to "Conos de seguridad de 45 cm(03) con cinta reflectiva(8-10cm)",
                        "eslingaGrilletes" to "Eslinga/Grilletes",
                        "picoPala" to "Pico y pala",
                        "cableCorriente" to "Cable para pasar corriente",
                        "extintor" to "Extintor PQS 6kg (ABC)",
                        "botiquinLinterna" to "Botiquín * / Linterna",
                        "kitAntiderrame" to "Kit antiderrame",
                        "sistemaComunicacion" to "Sistema Comunicación(Teléfono Satelital, Radio Handy, Celular)"
                    ), "verde"
                ),
                estadoMecanico = createEmptySection(
                    "Estado mecánico", listOf(
                        "pruebaFrenoServicio" to "Prueba del freno de servicio",
                        "frenoEstacionamiento" to "Freno de estacionamiento",
                        "direccion" to "Dirección",
                        "nivelAceiteMotor" to "Nivel de aceite del motor",
                        "nivelLiquidoFrenos" to "Nivel de liquido de frenos",
                        "nivelRefrigerante" to "Nivel de refrigerante",
                        "nivelAceiteHidraulico" to "Nivel de aceite hidráulico (hidrolina)",
                        "nivelAguaLimpiaparabrisas" to "Nivel de agua para limpiaparabrisas",
                        "otrosMecanico" to "Otros"
                    ), "rojo"
                ),
                sistemasCriticos = createEmptySection(
                    "Sistemas Criticos / ADAS", listOf(
                        "mobiliEye" to "MobiliEye",
                        "sensorFatiga" to "Sensor de Fatiga RDT401B (Sonido)",
                        "frenosABS" to "Frenos ABS",
                        "espEsc" to "ESP/ESC"
                    ), "rojo"
                ),
                cinturonesSeguridad = createEmptySection(
                    "Cinturones de Seguridad", listOf(
                        "cinturonesPilotos" to "Estado de Cinturones Pilotos",
                        "cinturonesPasajes" to "Estado de Cinturones Pasajes"
                    ), "rojo"
                )
            )
        )
    }

    private fun createEmptySection(
        sectionLabel: String,
        items: List<Pair<String, String>>,
        defaultColor: String
    ): InspectionSection {
        return InspectionSection(
            label = sectionLabel,
            items = items.associate { (key, label) ->
                key to InspectionItem(
                    label = label,
                    color = defaultColor,
                    value = false
                )
            }
        )
    }
}