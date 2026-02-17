package org.rol.transportation.presentation.checklist_document_inspection

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.rounded.Assignment
import androidx.compose.material.icons.rounded.CheckCircle
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
import org.rol.transportation.domain.model.document_inspection.DocumentInspection
import org.rol.transportation.domain.model.document_inspection.DocumentItem
import org.rol.transportation.domain.model.document_inspection.DocumentSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentInspectionScreen(
    vehiculoId: Int,
    tripId: Int,
    tipo: String,
    vehiculoChecklistDocumentId: Int?,
    onNavigateBack: () -> Unit,
    viewModel: DocumentInspectionViewModel = koinViewModel {
        parametersOf(vehiculoId, tripId, tipo, vehiculoChecklistDocumentId?: -1)
    }
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDark = isSystemInDarkTheme()
    val snackbarHostState = remember { SnackbarHostState() }

    // Estados para el diálogo de observación (IGUAL QUE EN LIGHTS ALARM)
    var showObservationDialog by remember { mutableStateOf(false) }
    var currentSectionKey by remember { mutableStateOf("") }
    var currentItemKey by remember { mutableStateOf("") }
    var currentItemLabel by remember { mutableStateOf("") }
    var currentObservationText by remember { mutableStateOf("") }


    if (uiState.successMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearSuccessMessage(); onNavigateBack() },
            icon = { Icon(Icons.Rounded.CheckCircle, null, Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary) },
            title = { Text("¡Éxito!", fontWeight = FontWeight.Bold) },
            text = { Text(uiState.successMessage ?: "", textAlign = TextAlign.Center) },
            containerColor = MaterialTheme.colorScheme.surface,
            confirmButton = {
                Button(onClick = { viewModel.clearSuccessMessage(); onNavigateBack() }) { Text("Aceptar", fontWeight = FontWeight.Bold) }
            }
        )
    }

    // Diálogo de Edición de Observación
    if (showObservationDialog) {
        AlertDialog(
            onDismissRequest = { showObservationDialog = false },
            title = { Text("Observación", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(currentItemLabel, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(16.dp))
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
                TextButton(onClick = {
                    viewModel.onObservationChanged(currentSectionKey, currentItemKey, currentObservationText)
                    showObservationDialog = false
                }) { Text("Guardar") }
            },
            dismissButton = { TextButton(onClick = { showObservationDialog = false }) { Text("Cancelar") } }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Inspección de Documentos", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge) },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = if (isDark) MaterialTheme.colorScheme.background else Color.Black, titleContentColor = Color.White, navigationIconContentColor = Color.White)
            )
        },
        bottomBar = {
            if (uiState.hasChanges && !uiState.isLoading) {
                Surface(shadowElevation = 8.dp, tonalElevation = 2.dp) {
                    Row(Modifier.fillMaxWidth().padding(16.dp)) {
                        Button(
                            onClick = viewModel::saveDocuments,
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
                                Text("GUARDAR DOCUMENTOS", fontWeight = FontWeight.Bold)
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
                uiState.error != null && uiState.documentInspection == null -> { /* Error View ... */ }

                uiState.documentInspection != null -> {
                    DocumentInspectionContent(
                        docs = uiState.documentInspection!!,
                        onItemChanged = viewModel::onItemChanged,
                        onEditObservation = { section, key, label, text ->
                            currentSectionKey = section
                            currentItemKey = key
                            currentItemLabel = label
                            currentObservationText = text
                            showObservationDialog = true
                        },
                        error = uiState.error,
                        onClearError = viewModel::clearError
                    )
                }
                else -> { Column(Modifier.align(Alignment.Center)) { Text("Cargando formulario...", color = Color.Gray) } }
            }
        }
    }
}

@Composable
fun DocumentInspectionContent(
    docs: DocumentInspection,
    onItemChanged: (String, String, Boolean) -> Unit,
    onEditObservation: (String, String, String, String) -> Unit, // Callback actualizado
    error: String?,
    onClearError: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        error?.let {  }

        DocumentSectionTable(
            section = docs.document.documentosVehiculo,
            sectionKey = "documentosVehiculo",
            onItemChanged = onItemChanged,
            onEditObservation = onEditObservation
        )

        DocumentSectionTable(
            section = docs.document.documentosConductor,
            sectionKey = "documentosConductor",
            onItemChanged = onItemChanged,
            onEditObservation = onEditObservation
        )

        Spacer(Modifier.height(80.dp))
    }
}

@Composable
fun DocumentSectionTable(
    section: DocumentSection,
    sectionKey: String,
    onItemChanged: (String, String, Boolean) -> Unit,
    onEditObservation: (String, String, String, String) -> Unit
) {
    val isDark = isSystemInDarkTheme()
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = if (!isDark) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null,
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.secondaryContainer).padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.Assignment, null, tint = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(text = section.label.uppercase(), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSecondaryContainer)
            }
            Column(Modifier.padding(8.dp)) {
                section.items.entries.forEachIndexed { index, (key, item) ->
                    DocumentTableRow(
                        item = item,
                        isLastItem = index == section.items.size - 1,
                        onValueChanged = { newValue -> onItemChanged(sectionKey, key, newValue) },
                        onEditObservation = { onEditObservation(sectionKey, key, item.label, item.observacion) }
                    )
                }
            }
        }
    }
}

@Composable
fun DocumentTableRow(
    item: DocumentItem,
    isLastItem: Boolean,
    onValueChanged: (Boolean) -> Unit,
    onEditObservation: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val colorIndicator = Color(0xFF42A5F5)

    // Verificamos si hay texto para pintar el icono
    val hasObservation = item.observacion.isNotBlank()

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onValueChanged(!item.habilitado) }
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(8.dp).clip(CircleShape).background(colorIndicator))
            Spacer(Modifier.width(12.dp))

            Text(
                text = item.label,
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
                checked = item.habilitado,
                onCheckedChange = onValueChanged,
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
            )
        }

        // Visualización de la observación en la fila
        if (hasObservation) {
            Text(
                text = "Obs: ${item.observacion}",
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