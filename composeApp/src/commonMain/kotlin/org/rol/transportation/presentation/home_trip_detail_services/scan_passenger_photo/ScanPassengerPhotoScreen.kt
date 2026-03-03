package org.rol.transportation.presentation.home_trip_detail_services.scan_passenger_photo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.CloudUpload
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.preat.peekaboo.ui.camera.PeekabooCamera
import com.preat.peekaboo.ui.camera.rememberPeekabooCameraState
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanPassengerPhotoScreen(
    tripId: Int,
    viajeTramoId: Int,
    onNavigateBack: () -> Unit,
    viewModel: ScanPassengerViewModel = koinViewModel { org.koin.core.parameter.parametersOf(tripId, viajeTramoId) }
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val factory = rememberPermissionsControllerFactory()
    val controller = remember(factory) { factory.createPermissionsController() }
    BindEffect(controller)

    val cameraState = rememberPeekabooCameraState(
        onCapture = { bytes ->
            bytes?.let {
                viewModel.onImageCaptured(it)
                // Notice we do NOT set showCamera = false here
            }
        }
    )

    // Launch camera automatically or show a button first. Let's do it immediately.
    LaunchedEffect(Unit) {
        try {
            controller.providePermission(Permission.CAMERA)
            viewModel.showCamera()
        } catch (e: Exception) {
            val result = snackbarHostState.showSnackbar(
                message = "Se requiere permiso de cámara",
                actionLabel = "AJUSTES",
                duration = SnackbarDuration.Long
            )
            if (result == SnackbarResult.ActionPerformed) {
                controller.openAppSettings()
            }
        }
    }

    // Temporary success text overlay logic
    var showSuccessOverlay by remember { mutableStateOf(false) }
    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            showSuccessOverlay = true
            val isProcessing = uiState.successMessage!!.contains("Procesando", ignoreCase = true)
            if (!isProcessing) {
                delay(2000)
                showSuccessOverlay = false
                viewModel.clearSuccessMessage()
                if (uiState.navigateBack) {
                    onNavigateBack()
                }
            }
        } else {
            showSuccessOverlay = false
        }
    }

    LaunchedEffect(uiState.error) {
        if (uiState.error != null && uiState.navigateBack) {
            delay(2500)
            viewModel.clearError()
            onNavigateBack()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color.Black
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {

            if (uiState.isCameraVisible) {
                PeekabooCamera(
                    state = cameraState,
                    modifier = Modifier.fillMaxSize(),
                    permissionDeniedContent = {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Sin permisos de cámara", color = Color.White)
                        }
                    }
                )

                // Back button overlay
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.padding(16.dp).padding(top = 24.dp).align(Alignment.TopStart).background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                }

                // Counter top right
                if (uiState.capturedImages.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .padding(top = 24.dp)
                            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${uiState.capturedImages.size} Fotos",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Dynamic UI Feedback
                val isProcessing = uiState.successMessage?.contains("Procesando", ignoreCase = true) == true
                val isErrorWithNav = uiState.error != null && uiState.navigateBack
                
                if ((showSuccessOverlay && uiState.successMessage != null) || isErrorWithNav) {
                    val message = uiState.successMessage ?: uiState.error ?: ""
                    val overlayColor = when {
                        isErrorWithNav -> Color.Red
                        isProcessing -> Color(0xFF03A9F4)
                        else -> Color.Green
                    }
                    val iconVector = if (isErrorWithNav) Icons.Rounded.Error else Icons.Rounded.CheckCircle
                    
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                            .padding(24.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            if (isProcessing) {
                                CircularProgressIndicator(color = overlayColor, modifier = Modifier.size(48.dp))
                            } else {
                                Icon(iconVector, contentDescription = null, tint = overlayColor, modifier = Modifier.size(48.dp))
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(message, color = overlayColor, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Bottom Controls
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                        .fillMaxWidth()
                ) {
                    // Capture Button
                    FloatingActionButton(
                        onClick = { cameraState.capture() },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                        modifier = Modifier.size(72.dp).align(Alignment.Center)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PhotoCamera,
                            contentDescription = "Capturar",
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Thumbnail bottom leading
                    if (uiState.capturedImages.isNotEmpty()) {
                        Image(
                            bitmap = uiState.capturedImages.last(),
                            contentDescription = "Última foto",
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 24.dp)
                                .size(64.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(2.dp, Color.White, RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Upload Button bottom trailing
                    if (uiState.capturedImages.isNotEmpty()) {
                        FloatingActionButton(
                            onClick = { viewModel.uploadPhotos() },
                            containerColor = Color(0xFF03A9F4),
                            contentColor = Color.White,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 24.dp)
                                .size(56.dp)
                        ) {
                            if (uiState.isUploading) {
                                CircularProgressIndicator(
                                    color = Color.White, 
                                    modifier = Modifier.size(24.dp), 
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Rounded.CloudUpload,
                                    contentDescription = "Subir fotos",
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                }

                // Error Overlay
                if (uiState.error != null && !uiState.navigateBack) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 80.dp)
                            .background(MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(uiState.error!!, color = MaterialTheme.colorScheme.onErrorContainer)
                            Spacer(Modifier.width(8.dp))
                            IconButton(onClick = viewModel::clearError, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Rounded.Close, null, tint = MaterialTheme.colorScheme.onErrorContainer)
                            }
                        }
                    }
                }

            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}
