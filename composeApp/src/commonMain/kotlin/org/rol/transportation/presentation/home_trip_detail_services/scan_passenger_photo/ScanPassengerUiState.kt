package org.rol.transportation.presentation.home_trip_detail_services.scan_passenger_photo

import androidx.compose.ui.graphics.ImageBitmap

data class ScanPassengerUiState(
    val isCameraVisible: Boolean = false,
    val capturedImages: List<ImageBitmap> = emptyList(),
    val capturedImageBytes: List<ByteArray> = emptyList(),
    val isUploading: Boolean = false,
    val uploadedUrls: List<String> = emptyList(),
    val error: String? = null,
    val successMessage: String? = null,
    val navigateBack: Boolean = false
)
