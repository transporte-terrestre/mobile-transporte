package org.rol.transportation.presentation.home_trip_detail_services

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.rol.transportation.data.remote.dto.trip_services.SegmentDto
import org.rol.transportation.data.remote.dto.trip_services.ProximoTramoDto
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.GpsFixed
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import org.rol.transportation.platform.MapView
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.Schedule
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripServicesScreen(
    tripId: Int,
    onNavigateBack: () -> Unit,
    viewModel: TripServicesViewModel = koinViewModel { parametersOf(tripId) }
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDark = isSystemInDarkTheme()

    val factory = rememberPermissionsControllerFactory()
    val permissionsController = remember(factory) { factory.createPermissionsController() }

    BindEffect(permissionsController)

    LaunchedEffect(Unit) {
        viewModel.requestPermissionAndStartLocation(permissionsController)
    }

    // Manejo de Mensajes
    if (uiState.createSuccess != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearMessages() },
            icon = { Icon(Icons.Rounded.CheckCircle, null, tint = MaterialTheme.colorScheme.primary) },
            title = { Text("Éxito") },
            text = { Text(uiState.createSuccess!!) },
            confirmButton = { Button(onClick = { viewModel.clearMessages() }) { Text("OK") } }
        )
    }

    // Bottom Sheet
    if (uiState.showLocationSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { viewModel.hideLocationSheet() },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            if (uiState.isLoadingProximoTramo) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(12.dp))
                        Text("Cargando datos...", style = MaterialTheme.typography.bodySmall)
                    }
                }
            } else {
                when (uiState.selectedAddOption) {
                    AddOption.PROXIMO -> {
                        if (uiState.proximoTramoData != null) {
                            ProximoTramoSheet(
                                data = uiState.proximoTramoData!!,
                                isCreating = uiState.isCreating,
                                onSave = { hora, km, pax ->
                                    viewModel.registrarServicio(hora, km, pax)
                                }
                            )
                        }
                    }
                    AddOption.OCASIONAL -> {
                        OcasionalSheet(
                            proximoData = uiState.proximoTramoData,
                            currentLocation = uiState.currentLocation,
                            isCreating = uiState.isCreating,
                            onSave = { nombre, hora, km, pax ->
                                viewModel.registrarParadaOcasional(nombre, hora, km, pax)
                            }
                        )
                    }
                    AddOption.DESCANSO -> {
                        DescansoSheet(
                            proximoData = uiState.proximoTramoData,
                            isCreating = uiState.isCreating,
                            onSave = { hora, km, pax ->
                                viewModel.registrarDescanso(hora, km, pax)
                            }
                        )
                    }
                    null -> {}
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Tramos", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDark) MaterialTheme.colorScheme.background else Color.Black,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            if (uiState.isFabVisible) {
                Box {
                    FloatingActionButton(
                        onClick = { viewModel.toggleAddMenu() },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    ) {
                        Icon(Icons.Default.Add, "Agregar")
                    }

                    DropdownMenu(
                        expanded = uiState.showAddMenu,
                        onDismissRequest = { viewModel.dismissAddMenu() }
                    ) {
                        if (uiState.isLoadingMenuProximo) {
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                                        Spacer(Modifier.width(8.dp))
                                        Text("Cargando...", fontWeight = FontWeight.SemiBold)
                                    }
                                },
                                onClick = {}
                            )
                        } else {
                            val tipoInfo = uiState.proximoTramoData?.tipo?.let { getTipoInfo(it) }
                                ?: getTipoInfo("punto")
                            
                            val menuLabel = when (uiState.proximoTramoData?.tipo?.lowercase()) {
                                "origen" -> "Iniciar"
                                "destino" -> "Terminar"
                                else -> "Próximo"
                            }

                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(tipoInfo.icon, null, tint = tipoInfo.color, modifier = Modifier.size(20.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Text(menuLabel, fontWeight = FontWeight.SemiBold)
                                    }
                                },
                                onClick = { viewModel.selectAddOption(AddOption.PROXIMO) }
                            )
                        }
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.PinDrop, null, tint = Color(0xFFFF9800), modifier = Modifier.size(20.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text("Ocasional", fontWeight = FontWeight.SemiBold)
                                }
                            },
                            onClick = { viewModel.selectAddOption(AddOption.OCASIONAL) }
                        )
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.PauseCircle, null, tint = Color(0xFF795548), modifier = Modifier.size(20.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text("Descanso", fontWeight = FontWeight.SemiBold)
                                }
                            },
                            onClick = { viewModel.selectAddOption(AddOption.DESCANSO) }
                        )
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            if (uiState.isLoading) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            } else if (uiState.segments.isEmpty()) {
                Text("No hay tramos registrados", Modifier.align(Alignment.Center), color = Color.Gray)
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    itemsIndexed(uiState.segments) { index, segment ->
                        ServiceItemCard(segment = segment, index = index)
                    }
                }
            }
        }
    }
}

// ===================== SHEET: PRÓXIMO =====================

@Composable
private fun ProximoTramoSheet(
    data: ProximoTramoDto,
    isCreating: Boolean,
    onSave: (String, Double, Int) -> Unit
) {
    val currentTimeStr = remember {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        "${now.hour.toString().padStart(2, '0')}:${now.minute.toString().padStart(2, '0')}"
    }

    var horaFinal by remember { mutableStateOf(currentTimeStr) }
    var kmFinal by remember { mutableStateOf(data.ultimoKilometraje?.let { if (it == it.toLong().toDouble()) it.toLong().toString() else it.toString() } ?: "") }
    var numPasajeros by remember { mutableStateOf(data.ultimosPasajeros?.toString() ?: "") }

    val lat = data.latitud?.toDoubleOrNull()
    val lng = data.longitud?.toDoubleOrNull()
    val tipoInfo = getTipoInfo(data.tipo)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(bottom = 32.dp)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(color = tipoInfo.color.copy(alpha = 0.15f), shape = CircleShape, modifier = Modifier.size(44.dp)) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(tipoInfo.icon, null, tint = tipoInfo.color, modifier = Modifier.size(24.dp))
                }
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(data.nombreLugar ?: "Próximo tramo", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(tipoInfo.label, style = MaterialTheme.typography.labelMedium, color = tipoInfo.color, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(Modifier.height(16.dp))

        // Mapa (retrasado para no interrumpir animación)
        var showMap by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            delay(400)
            showMap = true
        }

        if (lat != null && lng != null) {
            if (showMap) {
                Box(Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(12.dp))) {
                    MapView(latitude = lat, longitude = lng, title = data.nombreLugar ?: "Destino")
                }
            } else {
                Box(Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(12.dp)).background(Color.LightGray.copy(alpha=0.3f)), contentAlignment=Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        // Datos anteriores
        PreviousDataCard(
            hora = data.ultimaHora,
            km = data.ultimoKilometraje,
            pasajeros = data.ultimosPasajeros
        )

        Spacer(Modifier.height(20.dp))

        // Inputs
        InputSection(
            horaFinal = horaFinal, onHoraChange = { horaFinal = it },
            kmFinal = kmFinal, onKmChange = { kmFinal = it },
            numPasajeros = numPasajeros, onPaxChange = { numPasajeros = it }
        )

        Spacer(Modifier.height(20.dp))

        SaveButton(
            isCreating = isCreating,
            enabled = horaFinal.isNotBlank(),
            onClick = { 
                val km = kmFinal.replace(",", ".").replace(" ", "").replace("\u00A0", "").toDoubleOrNull() ?: 0.0
                onSave(horaFinal, km, numPasajeros.toIntOrNull() ?: 0) 
            }
        )
    }
}

// ===================== SHEET: OCASIONAL =====================

@Composable
private fun OcasionalSheet(
    proximoData: ProximoTramoDto?,
    currentLocation: org.rol.transportation.domain.model.LocationModel?,
    isCreating: Boolean,
    onSave: (String, String, Double, Int) -> Unit
) {
    val currentTimeStr = remember {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        "${now.hour.toString().padStart(2, '0')}:${now.minute.toString().padStart(2, '0')}"
    }

    var nombreLugar by remember { mutableStateOf("") }
    var horaFinal by remember { mutableStateOf(currentTimeStr) }
    var kmFinal by remember { mutableStateOf(proximoData?.ultimoKilometraje?.let { if (it == it.toLong().toDouble()) it.toLong().toString() else it.toString() } ?: "") }
    var numPasajeros by remember { mutableStateOf(proximoData?.ultimosPasajeros?.toString() ?: "") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(bottom = 32.dp)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(color = Color(0xFFFF9800).copy(alpha = 0.15f), shape = CircleShape, modifier = Modifier.size(44.dp)) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(Icons.Filled.PinDrop, null, tint = Color(0xFFFF9800), modifier = Modifier.size(24.dp))
                }
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Parada Ocasional", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Parada no programada", style = MaterialTheme.typography.labelMedium, color = Color(0xFFFF9800), fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(Modifier.height(16.dp))

        // Mapa (retrasado para no interrumpir animación)
        var showMap by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            delay(400)
            showMap = true
        }

        if (currentLocation != null) {
            if (showMap) {
                Box(Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(12.dp))) {
                    MapView(latitude = currentLocation.latitude, longitude = currentLocation.longitude, title = "Mi ubicación")
                }
            } else {
                Box(Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(12.dp)).background(Color.LightGray.copy(alpha=0.3f)), contentAlignment=Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        // Datos anteriores
        if (proximoData != null) {
            PreviousDataCard(
                hora = proximoData.ultimaHora,
                km = proximoData.ultimoKilometraje,
                pasajeros = proximoData.ultimosPasajeros
            )
            Spacer(Modifier.height(20.dp))
        }

        // Inputs
        Text("REGISTRAR DATOS", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface)
        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = nombreLugar,
            onValueChange = { nombreLugar = it },
            label = { Text("Nombre del lugar *") },
            placeholder = { Text("Ej: Grifo Primax km 45") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(10.dp))

        InputSection(
            horaFinal = horaFinal, onHoraChange = { horaFinal = it },
            kmFinal = kmFinal, onKmChange = { kmFinal = it },
            numPasajeros = numPasajeros, onPaxChange = { numPasajeros = it }
        )

        Spacer(Modifier.height(20.dp))

        SaveButton(
            isCreating = isCreating,
            enabled = horaFinal.isNotBlank() && nombreLugar.isNotBlank(),
            onClick = { 
                val km = kmFinal.replace(",", ".").replace(" ", "").replace("\u00A0", "").toDoubleOrNull() ?: 0.0
                onSave(nombreLugar, horaFinal, km, numPasajeros.toIntOrNull() ?: 0) 
            }
        )
    }
}

// ===================== SHEET: DESCANSO =====================

@Composable
private fun DescansoSheet(
    proximoData: ProximoTramoDto?,
    isCreating: Boolean,
    onSave: (String, Double, Int) -> Unit
) {
    var tiempoDescanso by remember { mutableStateOf("") }
    val now = remember { Clock.System.now() }
    val tz = remember { TimeZone.currentSystemDefault() }

    val startDateTime = remember { now.toLocalDateTime(tz) }
    val startStr = formatTime12h(startDateTime.hour, startDateTime.minute)

    val endInstant = remember(tiempoDescanso) {
        val mins = tiempoDescanso.toIntOrNull() ?: 0
        now + mins.minutes
    }
    val endDateTime = remember(endInstant) { endInstant.toLocalDateTime(tz) }
    val endStr = formatTime12h(endDateTime.hour, endDateTime.minute)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(bottom = 32.dp)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(color = Color(0xFF795548).copy(alpha = 0.15f), shape = CircleShape, modifier = Modifier.size(44.dp)) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(Icons.Filled.Coffee, null, tint = Color(0xFF795548), modifier = Modifier.size(24.dp))
                }
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Descanso", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Registro de descanso", style = MaterialTheme.typography.labelMedium, color = Color(0xFF795548), fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(Modifier.height(16.dp))

        // Inputs SIMPLES
        Text("TIEMPO DE DESCANSO", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color.Gray)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = tiempoDescanso,
            onValueChange = { tiempoDescanso = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = {
                Icon(Icons.Filled.Schedule, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
            },
            trailingIcon = {
                Text("MIN", style = MaterialTheme.typography.labelSmall, color = Color.Gray, modifier = Modifier.padding(end = 16.dp))
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(24.dp))

        // Label hora inicio y fin
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(startStr, style = MaterialTheme.typography.bodyMedium, color = Color.Gray, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.width(12.dp))
            HorizontalDivider(modifier = Modifier.width(16.dp), color = Color.LightGray)
            Spacer(Modifier.width(12.dp))
            Text(endStr, style = MaterialTheme.typography.bodyMedium, color = Color.Gray, fontWeight = FontWeight.SemiBold)
        }

        Spacer(Modifier.height(32.dp))

        SaveButton(
            isCreating = isCreating,
            enabled = tiempoDescanso.isNotBlank(),
            onClick = {
                val horaFinalStr = "${endDateTime.hour.toString().padStart(2, '0')}:${endDateTime.minute.toString().padStart(2, '0')}"
                val km = proximoData?.ultimoKilometraje ?: 0.0
                val pax = proximoData?.ultimosPasajeros ?: 0
                onSave(horaFinalStr, km, pax)
            }
        )
    }
}

// ===================== COMPONENTES REUTILIZABLES =====================

@Composable
private fun PreviousDataCard(hora: String?, km: Double?, pasajeros: Int?) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(
                "DATOS DEL TRAMO ANTERIOR",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                InfoLabel("Hora", formatHora(hora))
                InfoLabel("Kilometraje", if (km != null) "${formatKm(km)} KM" else "--")
                InfoLabel("Pasajeros", "${pasajeros ?: 0}")
            }
        }
    }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
private fun InputSection(
    horaFinal: String, onHoraChange: (String) -> Unit,
    kmFinal: String, onKmChange: (String) -> Unit,
    numPasajeros: String, onPaxChange: (String) -> Unit
) {
    var showTimePicker by remember { mutableStateOf(false) }

    if (showTimePicker) {
        val initialHour = horaFinal.substringBefore(":").toIntOrNull() ?: 12
        val initialMinute = horaFinal.substringAfter(":").toIntOrNull() ?: 0
        val timePickerState = androidx.compose.material3.rememberTimePickerState(
            initialHour = initialHour,
            initialMinute = initialMinute,
            is24Hour = true
        )

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showTimePicker = false
                    val formatted = "${timePickerState.hour.toString().padStart(2, '0')}:${timePickerState.minute.toString().padStart(2, '0')}"
                    onHoraChange(formatted)
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancelar")
                }
            },
            text = {
                androidx.compose.material3.TimePicker(state = timePickerState)
            }
        )
    }

    Text("REGISTRAR DATOS ACTUALES", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface)
    Spacer(Modifier.height(10.dp))

    Box(modifier = Modifier.fillMaxWidth().clickable { showTimePicker = true }) {
        OutlinedTextField(
            value = horaFinal,
            onValueChange = {},
            label = { Text("Hora de llegada (HH:mm) *") },
            readOnly = true,
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(Icons.Filled.Schedule, contentDescription = "Seleccionar hora")
            }
        )
    }
    Spacer(Modifier.height(10.dp))

    OutlinedTextField(
        value = kmFinal,
        onValueChange = onKmChange,
        label = { Text("Kilometraje actual") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
    Spacer(Modifier.height(10.dp))

    OutlinedTextField(
        value = numPasajeros,
        onValueChange = onPaxChange,
        label = { Text("N° de pasajeros") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Composable
private fun SaveButton(isCreating: Boolean, enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(50.dp),
        enabled = enabled && !isCreating,
        shape = RoundedCornerShape(12.dp)
    ) {
        if (isCreating) {
            CircularProgressIndicator(Modifier.size(24.dp), color = Color.White)
        } else {
            Text("Guardar", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun InfoLabel(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Spacer(Modifier.height(2.dp))
        Text(value, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
    }
}

// ===================== CARD DEL SERVICIO =====================

@Composable
private fun ServiceItemCard(segment: SegmentDto, index: Int) {
    val tipoInfo = getTipoInfo(segment.tipo)

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(color = tipoInfo.color.copy(alpha = 0.15f), shape = CircleShape, modifier = Modifier.size(40.dp)) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(tipoInfo.icon, segment.tipo, tint = tipoInfo.color, modifier = Modifier.size(22.dp))
                }
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(segment.nombreLugar ?: "Sin nombre", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.height(2.dp))
                Text(tipoInfo.label, style = MaterialTheme.typography.labelSmall, color = tipoInfo.color, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.width(8.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(formatHora(segment.horaFinal), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                if (segment.kilometrajeFinal != null) {
                    Text("${formatKm(segment.kilometrajeFinal)} KM", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                }
                if (segment.numeroPasajeros != null) {
                    Text("${segment.numeroPasajeros} pax", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                }
            }
        }
    }
}

// ===================== HELPERS =====================

private data class TipoInfo(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val color: Color
)

@Composable
private fun getTipoInfo(tipo: String): TipoInfo {
    return when (tipo.lowercase()) {
        "origen" -> TipoInfo("Origen", Icons.Filled.PlayArrow, Color(0xFF4CAF50))
        "destino" -> TipoInfo("Destino", Icons.Filled.Flag, Color(0xFFF44336))
        "punto" -> TipoInfo("Punto de Control", Icons.Filled.LocationOn, Color(0xFF2196F3))
        "parada" -> TipoInfo("Parada Ocasional", Icons.Filled.PinDrop, Color(0xFFFF9800))
        "descanso" -> TipoInfo("Descanso", Icons.Filled.Coffee, Color(0xFF795548))
        else -> TipoInfo(tipo.replaceFirstChar { it.uppercase() }, Icons.Filled.Place, Color.Gray)
    }
}

private fun formatTime12h(hour24: Int, minute: Int): String {
    val amPm = if (hour24 >= 12) "PM" else "AM"
    val hour = when {
        hour24 == 0 -> 12
        hour24 > 12 -> hour24 - 12
        else -> hour24
    }
    return "$hour:${minute.toString().padStart(2, '0')} $amPm"
}

private fun formatHora(isoDate: String?): String {
    if (isoDate == null) return "--:--"
    return try {
        val timePart = isoDate.substringAfter("T").substringBefore("Z").substringBefore(".")
        val parts = timePart.split(":")
        if (parts.size >= 2) "${parts[0]}:${parts[1]}" else timePart
    } catch (_: Exception) {
        isoDate
    }
}

private fun formatKm(km: Double): String {
    return if (km == km.toLong().toDouble()) {
        "%,d".format(km.toLong())
    } else {
        "%,.1f".format(km)
    }
}