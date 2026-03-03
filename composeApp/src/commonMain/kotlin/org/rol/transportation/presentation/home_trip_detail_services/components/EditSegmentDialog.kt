package org.rol.transportation.presentation.home_trip_detail_services.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.rol.transportation.data.remote.dto.trip_services.SegmentDto
import org.rol.transportation.data.remote.dto.trip_services.UpdateSegmentRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSegmentDialog(
    segment: SegmentDto,
    onDismissRequest: () -> Unit,
    onConfirm: (UpdateSegmentRequest) -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val tipoLower = segment.tipo.lowercase()

    // Determinamos qué campos se muestran y/o son editables
    val isOcasional = tipoLower == "parada"
    val isDescanso = tipoLower == "descanso"

    val allowsNombreLugar = isOcasional
    val allowsKilometrajePasajeros = !isDescanso

    // Inicializar variables desde el segment
    var nombreLugar by remember { mutableStateOf(segment.nombreLugar ?: "") }
    var kilometraje by remember { mutableStateOf(segment.kilometrajeFinal?.toString() ?: "") }

    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }

    // Parse existing time: "2024-03-24T10:30:00.000Z"
    LaunchedEffect(segment) {
        val dtString = segment.horaFinal
        if (dtString != null) {
            val parts = dtString.substringBefore("Z").split("T")
            if (parts.size == 2) {
                fecha = parts[0]
                hora = parts[1].take(5) // HH:mm
            }
        }
    }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(onClick = {
                val finalHora = if (fecha.isNotEmpty() && hora.isNotEmpty()) "${fecha}T${hora}:00Z" else null
                val req = UpdateSegmentRequest(
                    tipo = segment.tipo,
                    longitud = segment.longitud ?: 0.0,
                    latitud = segment.latitud ?: 0.0,
                    numeroPasajeros = segment.numeroPasajeros ?: 0,
                    nombreLugar = if (allowsNombreLugar) nombreLugar else (segment.nombreLugar ?: ""),
                    horaFinal = finalHora,
                    kilometrajeFinal = if (allowsKilometrajePasajeros) kilometraje.toDoubleOrNull() else (segment.kilometrajeFinal ?: 0.0)
                )
                onConfirm(req)
            }) {
                Text("Guardar Cambios")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) { Text("Cancelar") }
        },
        title = { Text("Editar Tramo: ${segment.tipo.capitalize()}") },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                if (allowsNombreLugar) {
                    OutlinedTextField(
                        value = nombreLugar,
                        onValueChange = { nombreLugar = it },
                        label = { Text("Nombre del Lugar") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Fecha y Hora UI
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(modifier = Modifier.weight(1f).clickable { showDatePicker = true }) {
                        OutlinedTextField(
                            value = fecha,
                            onValueChange = {},
                            label = { Text("Fecha", color = Color.Gray) },
                            readOnly = true,
                            enabled = false,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                disabledLabelColor = Color.Gray,
                                disabledTrailingIconColor = Color.Gray
                            ),
                            trailingIcon = { Icon(Icons.Filled.DateRange, contentDescription = null, tint = Color.Gray) }
                        )
                    }
                    Box(modifier = Modifier.weight(1f).clickable { showTimePicker = true }) {
                        OutlinedTextField(
                            value = hora,
                            onValueChange = {},
                            label = { Text("Hora", color = Color.Gray) },
                            readOnly = true,
                            enabled = false,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                disabledLabelColor = Color.Gray,
                                disabledTrailingIconColor = Color.Gray
                            ),
                            trailingIcon = { Icon(Icons.Rounded.AccessTime, contentDescription = null, tint = Color.Gray) }
                        )
                    }
                }

                if (allowsKilometrajePasajeros) {
                    OutlinedTextField(
                        value = kilometraje,
                        onValueChange = { kilometraje = it },
                        label = { Text("Kilometraje") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            }
        }
    )

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        AlertDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    datePickerState.selectedDateMillis?.let { millis ->
                        val instant = Instant.fromEpochMilliseconds(millis)
                        val localDate = instant.toLocalDateTime(TimeZone.UTC).date
                        fecha = localDate.toString()
                    }
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            },
            text = { DatePicker(state = datePickerState) }
        )
    }

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
}
