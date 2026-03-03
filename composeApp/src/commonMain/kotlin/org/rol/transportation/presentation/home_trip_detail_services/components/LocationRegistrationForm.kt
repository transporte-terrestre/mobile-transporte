package org.rol.transportation.presentation.home_trip_detail_services.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.material3.DatePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.TimePicker
import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.OutlinedTextFieldDefaults
import kotlinx.datetime.toLocalDateTime
import org.rol.transportation.data.remote.dto.trip_services.NextStepDto
import org.rol.transportation.domain.model.LocationModel
import org.rol.transportation.platform.MapView
import org.rol.transportation.utils.DateFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationRegistrationForm(
    title: String,
    isLoadingLocation: Boolean,
    isRegistering: Boolean,
    nextStepData: NextStepDto?,
    currentLocation: LocationModel?,
    buttonText: String,
    buttonColor: Color,
    isEditablePlaceName: Boolean = false,
    onNavigateBack: () -> Unit,
    onSubmit: (String, Double, String) -> Unit
) {
    val isDark = isSystemInDarkTheme()
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var kilometraje by remember { mutableStateOf("") }

    var nombreLugar by remember { mutableStateOf("") }

    LaunchedEffect(nextStepData) {
        if (nextStepData != null) {
            kilometraje = (nextStepData.ultimoKilometraje ?: 0.0).toString()
            if (nombreLugar.isBlank()) {
                nombreLugar = nextStepData.nombreLugar ?: ""
            }
        }
    }

    LaunchedEffect(Unit) {
        fecha = DateFormatter.getCurrentDateFormatted()
        hora = DateFormatter.getCurrentTimeFormatted()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDark) MaterialTheme.colorScheme.background else Color.Black,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(androidx.compose.foundation.rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            
            Text("LUGAR *", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            if (isEditablePlaceName) {
                OutlinedTextField(
                    value = nombreLugar,
                    onValueChange = { nombreLugar = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Ej. Villa el Salvador") }
                )
            } else {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Place, contentDescription = null, tint = buttonColor)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = nextStepData?.nombreLugar ?: "Ubicación GPS desconocida",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Text("UBICACIÓN (MAPA) *", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Card(
                modifier = Modifier.fillMaxWidth().height(250.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray)
            ) {
                if (isLoadingLocation) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (currentLocation != null) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        MapView(
                            latitude = currentLocation.latitude,
                            longitude = currentLocation.longitude,
                            title = "Mi Ubiación"
                        )
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No se pudo obtener la ubicación.")
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(title.uppercase(), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                val estimatedDate = nextStepData?.ultimaHora?.let { DateFormatter.formatOnlyDate(it) } ?: "--"
                val estimatedTime = nextStepData?.ultimaHora?.let { DateFormatter.formatOnlyTime(it) } ?: "--:--"
                Text("Estimado: $estimatedDate $estimatedTime", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }

            var showDatePicker by remember { mutableStateOf(false) }
            if (showDatePicker) {
                val datePickerState = rememberDatePickerState()
                AlertDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            showDatePicker = false
                            datePickerState.selectedDateMillis?.let { millis ->
                                val instant = kotlinx.datetime.Instant.fromEpochMilliseconds(millis)
                                val localDate = instant.toLocalDateTime(kotlinx.datetime.TimeZone.UTC).date
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

            OutlinedTextField(
                value = kilometraje,
                onValueChange = { kilometraje = it },
                label = { Text("KILOMETRAJE ACTUAL *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val combinedDateTime = "${fecha}T${hora}Z"
                    onSubmit(
                        combinedDateTime,
                        kilometraje.toDoubleOrNull() ?: 0.0,
                        if (isEditablePlaceName) nombreLugar else (nextStepData?.nombreLugar ?: "Ubicación GPS desconocida")
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = Color.White
                ),
                enabled = !isRegistering && currentLocation != null && kilometraje.isNotBlank() && (!isEditablePlaceName || nombreLugar.isNotBlank())
            ) {
                if (isRegistering) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text(buttonText, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
