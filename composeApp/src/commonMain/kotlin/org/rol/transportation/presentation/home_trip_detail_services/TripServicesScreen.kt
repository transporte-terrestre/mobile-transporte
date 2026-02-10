package org.rol.transportation.presentation.home_trip_detail_services

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.rol.transportation.data.remote.dto.trip_services.NextStepDto
import org.rol.transportation.data.remote.dto.trip_services.SegmentDto


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripServicesScreen(
    tripId: Int,
    onNavigateBack: () -> Unit,
    viewModel: TripServicesViewModel = koinViewModel { parametersOf(tripId) }
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDark = isSystemInDarkTheme()

    // Manejo de Mensajes (Toast/Dialog)
    if (uiState.createSuccess != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearMessages() },
            icon = { Icon(Icons.Rounded.CheckCircle, null, tint = MaterialTheme.colorScheme.primary) },
            title = { Text("Éxito") },
            text = { Text(uiState.createSuccess!!) },
            confirmButton = { Button(onClick = { viewModel.clearMessages() }) { Text("OK") } }
        )
    }

    // Modal de Creación
    if (uiState.showDialog && uiState.nextStepData != null) {
        CreateSegmentDialog(
            data = uiState.nextStepData!!,
            isCreating = uiState.isCreating,
            onDismiss = { viewModel.closeDialog() },
            onConfirm = { pPartida, pLlegada, hTermino, kmFin, numPasajeros, obs ->
                viewModel.createSegment(pPartida, pLlegada, hTermino, kmFin, numPasajeros, obs)
            }
        )
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
            if (uiState.isAddButtonVisible) {
                FloatingActionButton(
                    onClick = { viewModel.openCreateDialog() },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, "Agregar Tramo")
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            if (uiState.isLoading && !uiState.showDialog) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            } else if (uiState.segments.isEmpty()) {
                Text("No hay tramos registrados", Modifier.align(Alignment.Center), color = Color.Gray)
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.segments) { segment ->
                        SegmentCard(segment)
                    }
                }
            }
        }
    }
}

@Composable
fun SegmentCard(segment: SegmentDto) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = CircleShape) {
                    Text(
                        "${segment.orden}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    "${segment.paradaPartidaNombre} ➝ ${segment.paradaLlegadaNombre ?: "..."}",
                    fontWeight = FontWeight.Bold
                )
            }
            HorizontalDivider(Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(0.5f))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Salida", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text(segment.horaSalida, fontWeight = FontWeight.Bold)
                    Text("${segment.kmInicial} km", style = MaterialTheme.typography.bodySmall)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Llegada", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text(segment.horaTermino ?: "--:--", fontWeight = FontWeight.Bold)
                    Text("${segment.kmFinal ?: 0} km", style = MaterialTheme.typography.bodySmall)
                }
            }
            if (!segment.observaciones.isNullOrBlank()) {
                Spacer(Modifier.height(8.dp))
                Text("Obs: ${segment.observaciones}", style = MaterialTheme.typography.bodySmall, fontStyle = FontStyle.Italic)
            }
        }
    }
}

@Composable
fun CreateSegmentDialog(
    data: NextStepDto,
    isCreating: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int, String, Double, Int, String) -> Unit
) {
    // Estas variables conservan los datos que vienen del backend
    var paradaPartidaId by remember(data) { mutableStateOf(data.paradaPartidaId?.toString() ?: "") }
    var paradaLlegadaId by remember(data) { mutableStateOf(data.paradaLlegadaId?.toString() ?: "") }

    var horaTermino by remember { mutableStateOf("") }
    var kmFinal by remember { mutableStateOf("") }
    var pasajeros by remember(data) { mutableStateOf(data.numeroPasajeros?.toString() ?: "") }
    var observaciones by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Tramo: ${data.progreso ?: ""}") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(12.dp)) {
                        // ORIGEN
                        val origenTexto = data.paradaPartidaNombre ?: "ID $paradaPartidaId"
                        Text("De: $origenTexto", fontWeight = FontWeight.Bold)

                        // LLEGADA (Visualización solamente)
                        if (data.paradaLlegadaId != null) {
                            val llegadaTexto = data.paradaLlegadaNombre ?: "ID ${data.paradaLlegadaId}"
                            Text("A: $llegadaTexto", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }

                        HorizontalDivider(Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.onSecondaryContainer.copy(0.2f))

                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Salida: ${data.horaSalida ?: "--:-- "}", style = MaterialTheme.typography.bodySmall)
                            Text("KM Inicial: ${data.kmInicial ?: 0.0}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                // INPUT: ID PARTIDA (Solo se muestra si el backend NO mandó el ID)
                if (data.paradaPartidaId == null) {
                    OutlinedTextField(
                        value = paradaPartidaId,
                        onValueChange = { paradaPartidaId = it },
                        label = { Text("ID Origen (Requerido)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // INPUT: ID LLEGADA (Solo se muestra si el backend NO mandó el ID)
                // Si data.paradaLlegadaId ya tiene valor, este campo NO se dibuja, pero la variable 'paradaLlegadaId'
                // ya tiene el valor correcto gracias al 'remember' del inicio.
                if (data.paradaLlegadaId == null) {
                    OutlinedTextField(
                        value = paradaLlegadaId,
                        onValueChange = { paradaLlegadaId = it },
                        label = { Text("ID Llegada") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // --- AQUI ELIMINÉ EL TEXTFIELD DUPLICADO QUE MOSTRABA "ID Parada Llegada" SIEMPRE ---

                OutlinedTextField(
                    value = horaTermino,
                    onValueChange = { horaTermino = it },
                    label = { Text("Hora Término (HH:mm)") },
                    placeholder = { Text("07:45") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = kmFinal,
                    onValueChange = { kmFinal = it },
                    label = { Text("Kilometraje Final") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = pasajeros,
                    onValueChange = { pasajeros = it },
                    label = { Text("N° Pasajeros") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = observaciones,
                    onValueChange = { observaciones = it },
                    label = { Text("Observaciones") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        paradaPartidaId.toIntOrNull() ?: 0,
                        paradaLlegadaId.toIntOrNull() ?: 0, // Aquí se usa el valor interno oculto
                        horaTermino,
                        kmFinal.toDoubleOrNull() ?: 0.0,
                        pasajeros.toIntOrNull() ?: 0,
                        observaciones
                    )
                },
                // Validamos que exista un ID de partida para habilitar el botón
                enabled = !isCreating && paradaPartidaId.isNotBlank()
            ) {
                if (isCreating) CircularProgressIndicator(Modifier.size(24.dp), color = Color.White)
                else Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}