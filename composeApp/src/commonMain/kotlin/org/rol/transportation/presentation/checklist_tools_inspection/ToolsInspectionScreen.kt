package org.rol.transportation.presentation.checklist_tools_inspection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Edit
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.rol.transportation.domain.model.tools_inspection.ToolItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsInspectionScreen(
    vehiculoId: Int,
    tripId: Int,
    tipo: String,
    vehiculoChecklistDocumentId: Int?,
    onNavigateBack: () -> Unit,
    viewModel: ToolsInspectionViewModel = koinViewModel { parametersOf(vehiculoId, tripId, tipo, vehiculoChecklistDocumentId ?: -1) }
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDark = isSystemInDarkTheme()
    val snackbarHostState = remember { SnackbarHostState() }

    // Estado para el Diálogo de Detalles
    var showDetailDialog by remember { mutableStateOf(false) }
    var currentKey by remember { mutableStateOf("") }
    // Este objeto temporal almacenará los cambios DENTRO del diálogo antes de confirmar
    var tempItem by remember { mutableStateOf<ToolItem?>(null) }

    // Success Dialog
    if (uiState.successMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearMessages(); onNavigateBack() },
            icon = { Icon(Icons.Rounded.CheckCircle, null, Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary) },
            title = { Text("¡Éxito!", fontWeight = FontWeight.Bold) },
            text = { Text(uiState.successMessage ?: "") },
            confirmButton = { Button(onClick = { viewModel.clearMessages(); onNavigateBack() }) { Text("Aceptar") } }
        )
    }

    // --- DIÁLOGO DE DETALLES (Criterios, Stock, etc) ---
    if (showDetailDialog && tempItem != null) {
        val criteriaLabels = uiState.inspection?.criteriaLabels ?: emptyMap()

        AlertDialog(
            onDismissRequest = { showDetailDialog = false },
            title = { Text(tempItem!!.label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxWidth()
                ) {
                    Text("Detalles y Criterios", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))

                    // Stock
                    OutlinedTextField(
                        value = tempItem!!.stock,
                        onValueChange = { tempItem = tempItem!!.copy(stock = it) },
                        label = { Text("Stock") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))

                    // Criterios (Checkboxes con etiquetas dinámicas)
                    CriteriaCheckbox(criteriaLabels["criterioA"] ?: "Criterio A", tempItem!!.criterioA) { tempItem = tempItem!!.copy(criterioA = it) }
                    CriteriaCheckbox(criteriaLabels["criterioB"] ?: "Criterio B", tempItem!!.criterioB) { tempItem = tempItem!!.copy(criterioB = it) }
                    CriteriaCheckbox(criteriaLabels["criterioC"] ?: "Criterio C", tempItem!!.criterioC) { tempItem = tempItem!!.copy(criterioC = it) }
                    CriteriaCheckbox(criteriaLabels["criterioD"] ?: "Criterio D", tempItem!!.criterioD) { tempItem = tempItem!!.copy(criterioD = it) }
                    CriteriaCheckbox(criteriaLabels["criterioE"] ?: "Criterio E", tempItem!!.criterioE) { tempItem = tempItem!!.copy(criterioE = it) }
                    CriteriaCheckbox(criteriaLabels["criterioF"] ?: "Criterio F", tempItem!!.criterioF) { tempItem = tempItem!!.copy(criterioF = it) }

                    Spacer(Modifier.height(8.dp))

                    // Acción Correctiva
                    OutlinedTextField(
                        value = tempItem!!.accionCorrectiva,
                        onValueChange = { tempItem = tempItem!!.copy(accionCorrectiva = it) },
                        label = { Text("Acción Correctiva") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    // Observación
                    OutlinedTextField(
                        value = tempItem!!.observacion,
                        onValueChange = { tempItem = tempItem!!.copy(observacion = it) },
                        label = { Text("Observación") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    // Guardar cambios del temporal al ViewModel
                    tempItem?.let { newItem ->
                        viewModel.updateItem(currentKey) { newItem }
                    }
                    showDetailDialog = false
                }) { Text("Guardar Detalles") }
            },
            dismissButton = {
                TextButton(onClick = { showDetailDialog = false }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Herramientas", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = if (isDark) MaterialTheme.colorScheme.background else Color.Black, titleContentColor = Color.White, navigationIconContentColor = Color.White)
            )
        },
        bottomBar = {
            if (uiState.hasChanges && !uiState.isLoading) {
                Surface(shadowElevation = 8.dp) {
                    Row(Modifier.padding(16.dp)) {
                        Button(onClick = viewModel::save, modifier = Modifier.fillMaxWidth().height(56.dp), enabled = !uiState.isSaving) {
                            if (uiState.isSaving) CircularProgressIndicator(color = Color.White) else Text("GUARDAR CAMBIOS")
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
                uiState.inspection != null -> {
                    ToolsContent(
                        items = uiState.inspection!!.items,
                        onToggleState = { key -> viewModel.updateItem(key) { it.copy(estado = !it.estado) } },
                        onEditDetails = { key, item ->
                            currentKey = key
                            tempItem = item
                            showDetailDialog = true
                        }
                    )
                }
                else -> Text("Cargando...", Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun ToolsContent(
    items: Map<String, ToolItem>,
    onToggleState: (String) -> Unit,
    onEditDetails: (String, ToolItem) -> Unit
) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.secondaryContainer).padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Rounded.Build, null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
                    Spacer(Modifier.width(8.dp))
                    Text("LISTA DE HERRAMIENTAS", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSecondaryContainer)
                }
                Column(Modifier.padding(8.dp)) {
                    items.entries.forEachIndexed { index, (key, item) ->
                        ToolRow(
                            item = item,
                            isLastItem = index == items.size - 1,
                            onToggle = { onToggleState(key) },
                            onEdit = { onEditDetails(key, item) }
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(80.dp))
    }
}

@Composable
fun ToolRow(
    item: ToolItem,
    isLastItem: Boolean,
    onToggle: () -> Unit,
    onEdit: () -> Unit
) {
    val colorIndicator = Color(0xFF42A5F5)

    val hasDetails = item.stock.isNotBlank() || item.observacion.isNotBlank() || item.accionCorrectiva.isNotBlank()

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(8.dp).clip(CircleShape).background(colorIndicator))
            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(item.label, style = MaterialTheme.typography.bodyMedium)
                if (hasDetails) {
                    Text("Con detalles guardados", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                }
            }

            // Botón de Edición (Lápiz) para abrir el diálogo completo
            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = "Editar Detalles",
                    tint = if (hasDetails) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary
                )
            }

            Checkbox(
                checked = item.estado,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
            )
        }
        if (!isLastItem) HorizontalDivider(Modifier.padding(horizontal = 8.dp), thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    }
}

@Composable
fun CriteriaCheckbox(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onCheckedChange(!checked) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange, modifier = Modifier.scale(0.8f))
        Text(text = label, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
    }
}