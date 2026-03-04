package org.rol.transportation.presentation.home_trip_detail_services.register_stop

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.rol.transportation.utils.DateFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterStopScreen(
    tripId: Int,
    onNavigateBack: () -> Unit,
    viewModel: RegisterStopViewModel = koinViewModel { parametersOf(tripId) }
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDark = isSystemInDarkTheme()

    if (uiState.showGpsDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissGpsDialog() },
            title = { Text("GPS Desactivado") },
            text = { Text("Activa los servicios de ubicación (GPS) en tu dispositivo.") },
            confirmButton = { TextButton(onClick = { viewModel.openLocationSettings() }) { Text("Configuración") } },
            dismissButton = { TextButton(onClick = { viewModel.dismissGpsDialog() }) { Text("Cancelar") } }
        )
    }

    if (uiState.error != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearMessages() },
            title = { Text("Error") },
            text = { Text(uiState.error ?: "") },
            confirmButton = { TextButton(onClick = { viewModel.clearMessages() }) { Text("Aceptar") } }
        )
    }
    
    val sheetColor = if (isDark) Color(0xFF1E1E1E) else MaterialTheme.colorScheme.surface
    val textColor = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface
    val labelColor = if (isDark) Color.Gray else MaterialTheme.colorScheme.onSurfaceVariant
    val inputBgColor = if (isDark) Color.DarkGray else MaterialTheme.colorScheme.outlineVariant
    val prevDataBgColor = if (isDark) Color(0xFF2C2C2C) else MaterialTheme.colorScheme.surfaceVariant
    val buttonDisabledColor = if (isDark) Color(0xFF1E1E1E) else MaterialTheme.colorScheme.surfaceVariant
    val buttonEnabledColor = if (isDark) Color(0xFFFFC107) else Color(0xFFFFB300) // Amarillo oscuro/claro
    val buttonContentColor = Color.Black

    LaunchedEffect(Unit) {
        viewModel.startLocation()
    }

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            onNavigateBack()
            viewModel.clearMessages()
        }
    }

    var nombreLugar by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var kilometraje by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        hora = DateFormatter.getCurrentHourAndMinute()
    }

    LaunchedEffect(uiState.nextStepData) {
        uiState.nextStepData?.ultimoKilometraje?.let { km ->
            kilometraje = km.toString()
        }
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onNavigateBack,
        sheetState = sheetState,
        containerColor = sheetColor,
        scrimColor = Color.Transparent, // The dialog route already dims the background
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color.Gray.copy(alpha = 0.5f)) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {

                // Header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFFFA000).copy(alpha = 0.2f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Place, contentDescription = null, tint = Color(0xFFFFA000), modifier = Modifier.size(24.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Parada Ocasional",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                            color = textColor
                        )
                        Text(
                            text = "Parada no programada",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFFFA000)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                // Mapa
                if (uiState.isLoading || uiState.isLocationLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        org.rol.transportation.presentation.map.MapLoadingOverlay(modifier = Modifier.fillMaxSize())
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                } else if (uiState.currentLocation != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        org.rol.transportation.presentation.map.MapViewWithLoading(
                            latitude = uiState.currentLocation!!.latitude,
                            longitude = uiState.currentLocation!!.longitude,
                            title = "Mi Ubicación",
                            modifier = Modifier.fillMaxSize(),
                            isLiteMode = true,
                            loadingDuration = 0L
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Datos Tramo Anterior
                Text(
                    text = "DATOS DEL TRAMO ANTERIOR",
                    style = MaterialTheme.typography.labelMedium,
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = prevDataBgColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Hora", style = MaterialTheme.typography.labelSmall, color = labelColor)
                            val prevHora = uiState.nextStepData?.ultimaHora?.substringAfter("T")?.take(5) ?: "--:--"
                            Text(prevHora, style = MaterialTheme.typography.titleMedium, color = textColor, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Kilometraje", style = MaterialTheme.typography.labelSmall, color = labelColor)
                            val prevKm = uiState.nextStepData?.ultimoKilometraje ?: 0.0
                            Text("$prevKm KM", style = MaterialTheme.typography.titleMedium, color = textColor, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Pasajeros", style = MaterialTheme.typography.labelSmall, color = labelColor)
                            val prevPax = uiState.nextStepData?.ultimosPasajeros ?: 0
                            Text("$prevPax", style = MaterialTheme.typography.titleMedium, color = textColor, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "REGISTRAR DATOS",
                    style = MaterialTheme.typography.labelMedium,
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = nombreLugar,
                    onValueChange = { nombreLugar = it },
                    label = { Text("Nombre del lugar *", color = labelColor) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFFA000),
                        unfocusedBorderColor = inputBgColor,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        cursorColor = Color(0xFFFFA000)
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "REGISTRAR DATOS ACTUALES",
                    style = MaterialTheme.typography.labelMedium,
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                var showTimePicker by remember { mutableStateOf(false) }
                if (showTimePicker) {
                    val initialHour = hora.substringBefore(":").toIntOrNull() ?: 12
                    val initialMinute = hora.substringAfter(":").toIntOrNull() ?: 0
                    val timePickerState = rememberTimePickerState(
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
                                hora = formatted
                            }) { Text("Aceptar") }
                        },
                        dismissButton = {
                            TextButton(onClick = { showTimePicker = false }) { Text("Cancelar") }
                        },
                        text = { TimePicker(state = timePickerState) }
                    )
                }

                Box(modifier = Modifier.fillMaxWidth().clickable { showTimePicker = true }) {
                    OutlinedTextField(
                        value = hora,
                        onValueChange = {},
                        label = { Text("Hora de llegada (HH:mm) *", color = labelColor) },
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = textColor,
                            disabledBorderColor = inputBgColor,
                            disabledLabelColor = labelColor,
                            disabledTrailingIconColor = labelColor
                        ),
                        trailingIcon = { Icon(Icons.Rounded.AccessTime, contentDescription = null, tint = labelColor) }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = kilometraje,
                    onValueChange = { kilometraje = it },
                    label = { Text("Kilometraje actual", color = labelColor) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFFA000),
                        unfocusedBorderColor = inputBgColor,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        cursorColor = Color(0xFFFFA000)
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                val isValid = nombreLugar.isNotBlank() && hora.isNotBlank() && uiState.currentLocation != null
                Button(
                    onClick = {
                        val km = kilometraje.toDoubleOrNull() ?: 0.0
                        val horaCompleta = DateFormatter.getFullDateTimeStringWithHour(hora)
                        viewModel.registerStop(horaActual = horaCompleta, kilometrajeActual = km, cantidadPasajeros = 0, nombreLugar = nombreLugar)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isValid) buttonEnabledColor else buttonDisabledColor,
                        contentColor = if (isValid) buttonContentColor else labelColor
                    ),
                    enabled = isValid && !uiState.isRegistering
                ) {
                    if (uiState.isRegistering) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.Black, strokeWidth = 2.dp)
                    } else {
                        Text(if (uiState.currentLocation == null) "Obteniendo GPS..." else "Guardar", fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

}
