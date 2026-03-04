package org.rol.transportation.presentation.home_trip_detail_services.scan_passenger_photo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.preat.peekaboo.image.picker.toImageBitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.rol.transportation.domain.usecase.ScanDnisPhotosUseCase
import org.rol.transportation.domain.usecase.UploadImageUseCase
import org.rol.transportation.utils.AppEventBus
import org.rol.transportation.utils.ImageCompressor
import org.rol.transportation.utils.Resource
import kotlin.time.Clock

class ScanPassengerViewModel(
    private val tripId: Int,
    private val viajeTramoId: Int,
    private val uploadImageUseCase: UploadImageUseCase,
    private val scanDnisPhotosUseCase: ScanDnisPhotosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScanPassengerUiState())
    val uiState: StateFlow<ScanPassengerUiState> = _uiState.asStateFlow()

    fun showCamera() {
        _uiState.update { it.copy(isCameraVisible = true) }
    }

    fun hideCamera() {
        _uiState.update { it.copy(isCameraVisible = false) }
    }

    fun onImageCaptured(imageBytes: ByteArray) {
        viewModelScope.launch {
            try {
                // Compress image
                val compressedBytes = ImageCompressor.compress(imageBytes)

                if (compressedBytes.size > 5 * 1024 * 1024) {
                    _uiState.update { it.copy(error = "Imagen demasiado grande") }
                    return@launch
                }

                val imageBitmap = compressedBytes.toImageBitmap()
                
                val updatedList = _uiState.value.capturedImages + imageBitmap
                val updatedBytesList = _uiState.value.capturedImageBytes + compressedBytes
                _uiState.update {
                    it.copy(
                        capturedImages = updatedList,
                        capturedImageBytes = updatedBytesList,
                        error = null,
                        successMessage = "Foto Tomada!"
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error: ${e.message}") }
            }
        }
    }

    fun uploadPhotos() {
        viewModelScope.launch {
            val total = _uiState.value.capturedImageBytes.size
            if (total == 0) {
                 _uiState.update { it.copy(error = "No hay fotos para subir") }
                 return@launch
            }
            
            _uiState.update { it.copy(isUploading = true, error = null) }
            val urls = mutableListOf<String>()
            
            _uiState.value.capturedImageBytes.forEachIndexed { index, bytes ->
                val fileName = "passenger_${Clock.System.now().toEpochMilliseconds()}_$index.jpg"
                uploadImageUseCase(bytes, fileName, "dnis").collect { result ->
                     when (result) {
                         is Resource.Success -> {
                             urls.add(result.data.url)
                         }
                         is Resource.Error -> {
                             // Error manejado globalmente abajo
                         }
                         is Resource.Loading -> {}
                     }
                }
            }
            
            if (urls.size == total) {
                 _uiState.update { 
                     it.copy(
                         isUploading = true,
                         uploadedUrls = urls,
                         successMessage = "Procesando fotos..." // UI update before API calling wait
                     ) 
                 }
                 scanDnis(urls)
            } else {
                 _uiState.update { 
                     it.copy(
                         isUploading = false, 
                         error = "Error: se subieron ${urls.size} de $total fotos.",
                         navigateBack = true
                     ) 
                 }
            }
        }
    }

    private fun scanDnis(urls: List<String>) {
        viewModelScope.launch {
            scanDnisPhotosUseCase(tripId, viajeTramoId, urls).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isUploading = true, error = null) }
                    }
                    is Resource.Success -> {
                        val response = result.data
                        val updatedCount = response.pasajerosActualizados.size
                        _uiState.update { 
                            it.copy(
                                isUploading = false,
                                successMessage = response.mensaje,
                                capturedImages = emptyList(), // Optionally clear images
                                capturedImageBytes = emptyList(),
                                uploadedUrls = emptyList(),
                                navigateBack = true
                            ) 
                        }
                        AppEventBus.triggerReload()
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(isUploading = false, error = result.message, navigateBack = true) }
                    }
                }
            }
        }
    }

    fun clearSuccessMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
