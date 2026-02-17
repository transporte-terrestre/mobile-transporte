package org.rol.transportation.presentation.checklist_lights_alarm

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Note
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.rol.transportation.domain.model.lights_alarm.LightItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LightsAlarmScreen(
    vehiculoId: Int,
    tripId: Int,
    tipo: String,
    vehiculoChecklistDocumentId: Int?,
    onNavigateBack: () -> Unit,
    viewModel: LightsAlarmViewModel = koinViewModel {
        parametersOf(vehiculoId, tripId, tipo, vehiculoChecklistDocumentId ?: -1)
    }
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDark = isSystemInDarkTheme()
    val snackbarHostState = remember { SnackbarHostState() }

    // Estados para manejar el diálogo de observación
    var showObservationDialog by remember { mutableStateOf(false) }
    var currentItemKey by remember { mutableStateOf("") }
    var currentItemLabel by remember { mutableStateOf("") }
    var currentObservationText by remember { mutableStateOf("") }


    if (uiState.successMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearMessages(); onNavigateBack() },
            icon = { Icon(Icons.Rounded.CheckCircle, null, Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary) },
            title = { Text("¡Éxito!", fontWeight = FontWeight.Bold) },
            text = { Text(uiState.successMessage ?: "", textAlign = TextAlign.Center) },
            containerColor = MaterialTheme.colorScheme.surface,
            confirmButton = {
                Button(onClick = { viewModel.clearMessages(); onNavigateBack() }) { Text("Aceptar", fontWeight = FontWeight.Bold) }
            }
        )
    }

    // Diálogo para editar Observación
    if (showObservationDialog) {
        AlertDialog(
            onDismissRequest = { showObservationDialog = false },
            title = { Text(text = "Observación", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(text = currentItemLabel, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = currentObservationText,
                        onValueChange = { currentObservationText = it },
                        label = { Text("Ingrese detalles") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onObservationChanged(currentItemKey, currentObservationText)
                        showObservationDialog = false
                    }
                ) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { showObservationDialog = false }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Luces y Alarmas", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge) },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = if (isDark) MaterialTheme.colorScheme.background else Color.Black, titleContentColor = Color.White, navigationIconContentColor = Color.White)
            )
        },
        bottomBar = {
            if (uiState.hasChanges && !uiState.isLoading) {
                Surface(shadowElevation = 8.dp, tonalElevation = 2.dp) {
                    Row(Modifier.fillMaxWidth().padding(16.dp)) {
                        Button(
                            onClick = viewModel::save,
                            modifier = Modifier.weight(1f).height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !uiState.isSaving
                        ) {
                            if (uiState.isSaving) {
                                CircularProgressIndicator(Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                                Spacer(Modifier.width(12.dp))
                                Text("Guardando...")
                            } else {
                                Icon(Icons.Rounded.Save, null)
                                Spacer(Modifier.width(8.dp))
                                Text("GUARDAR CAMBIOS", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                uiState.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))

                uiState.error != null && uiState.inspection == null -> {
                    Column(Modifier.align(Alignment.Center).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Rounded.ErrorOutline, null, Modifier.size(64.dp), tint = MaterialTheme.colorScheme.error)
                        Text("Ocurrió un error", style = MaterialTheme.typography.titleMedium)
                        Text(uiState.error ?: "", textAlign = TextAlign.Center)
                        Button(onClick = onNavigateBack) { Text("Volver") }
                    }
                }

                uiState.inspection != null -> {
                    LightsAlarmContent(
                        items = uiState.inspection!!.items,
                        onItemChanged = viewModel::onItemChanged,
                        onEditObservation = { key, label, currentText ->
                            currentItemKey = key
                            currentItemLabel = label
                            currentObservationText = currentText
                            showObservationDialog = true
                        }
                    )
                }

                else -> {
                    Column(modifier = Modifier.align(Alignment.Center)) {
                        Text("Cargando formulario...", color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun LightsAlarmContent(
    items: Map<String, LightItem>,
    onItemChanged: (String, Boolean) -> Unit,
    onEditObservation: (String, String, String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.secondaryContainer).padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Rounded.LightMode, null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
                    Spacer(Modifier.width(8.dp))
                    Text("VERIFICACIÓN DE COMPONENTES", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSecondaryContainer)
                }

                Column(Modifier.padding(8.dp)) {
                    items.entries.forEachIndexed { index, (key, item) ->
                        LightsTableRow(
                            label = item.label,
                            isChecked = item.estado,
                            observation = item.observacion,
                            isLastItem = index == items.size - 1,
                            onValueChanged = { onItemChanged(key, it) },
                            onEditObservation = { onEditObservation(key, item.label, item.observacion) }
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(80.dp))
    }
}

@Composable
fun LightsTableRow(
    label: String,
    isChecked: Boolean,
    observation: String,
    isLastItem: Boolean,
    onValueChanged: (Boolean) -> Unit,
    onEditObservation: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    // Color por defecto (Azul) para mantener consistencia con las otras tablas
    val colorIndicator = Color(0xFF42A5F5)

    // Determinar si hay observación para pintar el ícono
    val hasObservation = observation.isNotBlank()

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onValueChanged(!isChecked) }
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(colorIndicator))

            Spacer(Modifier.width(12.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = { onEditObservation() }) {
                Icon(
                    imageVector = if (hasObservation) Icons.AutoMirrored.Rounded.Note else Icons.AutoMirrored.Rounded.Note,
                    contentDescription = "Observación",
                    tint = if (hasObservation) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary
                )
            }

            Checkbox(
                checked = isChecked,
                onCheckedChange = onValueChanged,
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
            )
        }


        if (hasObservation) {
            Text(
                text = "Obs: $observation",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 28.dp, bottom = 8.dp, end = 16.dp),
                maxLines = 2
            )
        }

        if (!isLastItem) {
            HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp), thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        }
    }
}