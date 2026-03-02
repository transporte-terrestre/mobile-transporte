package org.rol.transportation.presentation.home_trip_detail_services.register_rest

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.rol.transportation.utils.DateFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterRestScreen(
    tripId: Int,
    onNavigateBack: () -> Unit,
    viewModel: RegisterRestViewModel = koinViewModel { parametersOf(tripId) }
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

    var minutosRest by remember { mutableStateOf("") }
    var startTimeString by remember { mutableStateOf("") }
    var endTimeString by remember { mutableStateOf("") }

    LaunchedEffect(minutosRest) {
        val addedMinutes = minutosRest.toIntOrNull() ?: 0
        val times = DateFormatter.getCalculatedRestTimes(addedMinutes)
        startTimeString = times.first
        endTimeString = times.second
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onNavigateBack,
        sheetState = sheetState,
        containerColor = sheetColor,
        scrimColor = Color.Transparent, // Transparent because dialog already dims
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
                        color = Color(0xFF795548).copy(alpha = 0.2f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.LocalCafe, contentDescription = null, tint = Color(0xFF795548), modifier = Modifier.size(24.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Descanso",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                            color = textColor
                        )
                        Text(
                            text = "Registro de descanso",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF795548)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "TIEMPO DE DESCANSO",
                    style = MaterialTheme.typography.labelMedium,
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = minutosRest,
                    onValueChange = { minutosRest = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = { Icon(Icons.Rounded.AccessTime, contentDescription = null, tint = labelColor) },
                    trailingIcon = { Text("MIN", color = labelColor, modifier = Modifier.padding(end = 12.dp)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF795548),
                        unfocusedBorderColor = inputBgColor,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        cursorColor = Color(0xFF795548)
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "$startTimeString   —   $endTimeString",
                    color = labelColor,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                val isValid = uiState.currentLocation != null
                Button(
                    onClick = {
                        val prevKm = uiState.nextStepData?.ultimoKilometraje ?: 0.0
                        val horaCompleta = DateFormatter.getFullDateTimeStringWithHour(DateFormatter.getCurrentHourAndMinute())
                        
                        viewModel.registerRest(
                            horaActual = horaCompleta,
                            kilometrajeActual = prevKm,
                            cantidadPasajeros = 0,
                            nombreLugar = "Descanso ${minutosRest.takeIf { it.isNotBlank() } ?: "0"} min"
                        )
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
