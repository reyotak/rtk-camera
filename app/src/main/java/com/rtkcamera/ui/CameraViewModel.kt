package com.rtkcamera.ui

import android.content.Context
import androidx.camera.core.ImageCapture
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rtkcamera.camera.AnalysisPipeline
import com.rtkcamera.nativebridge.AlgorithmInfo
import com.rtkcamera.nativebridge.NativeLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.rtkcamera.camera.CameraManager
import com.rtkcamera.camera.CapturePipeline

/**
 * UI State for the Camera Screen.
 * Matches the Data Model.
 */
data class CameraUiState(
    val hasCameraPermission: Boolean = false,
    val cameraError: String? = null,
    val activeAlgorithmId: String? = null,
    val availableAlgorithms: List<AlgorithmInfo> = emptyList(),
    val isProcessing: Boolean = false,
    val lastProcessingTimeMs: Long = 0,
    val performanceWarning: String? = null // Displayed if processing > 33ms (Principle III)
)

/**
 * ViewModel for managing camera UI state and algorithm selection.
 */
@HiltViewModel
class CameraViewModel @Inject constructor(
    private val nativeLoader: NativeLoader,
    private val analysisPipeline: AnalysisPipeline,
    private val capturePipeline: CapturePipeline,
    private val cameraManager: CameraManager
) : ViewModel() {

    private val _cameraProviderFlow = MutableStateFlow<ProcessCameraProvider?>(null)
    val cameraProviderFlow = _cameraProviderFlow.asStateFlow()

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Background loop to monitor performance metrics
        viewModelScope.launch {
            while (true) {
                val lastTime = analysisPipeline.getLastProcessingTimeMs()
                val warning = if (lastTime > 33) {
                    val fps = if (lastTime > 0) 1000 / lastTime else 0
                    "Low FPS Warning: $fps fps ($lastTime ms)"
                } else null
                
                _uiState.value = _uiState.value.copy(
                    lastProcessingTimeMs = lastTime,
                    performanceWarning = warning
                )
                delay(500) // Update metrics every 500ms to avoid UI jitter
            }
        }
    }

    /**
     * Handles algorithm selection from the UI.
     */
    fun onAlgorithmSelected(algorithm: AlgorithmInfo) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isProcessing = true)
            val success = nativeLoader.load(algorithm)
            _uiState.value = _uiState.value.copy(
                activeAlgorithmId = if (success) algorithm.id else null,
                isProcessing = false
            )
        }
    }

    /**
     * Handles manual capture trigger.
     */
    fun onCaptureTriggered(imageCapture: ImageCapture, context: Context) {
        capturePipeline.takeSnapshot(imageCapture, ContextCompat.getMainExecutor(context))
    }

    fun onPermissionResult(granted: Boolean) {
        _uiState.value = _uiState.value.copy(hasCameraPermission = granted, cameraError = if (granted) null else "Camera permission denied")
        if (granted) {
            viewModelScope.launch {
                try {
                    _cameraProviderFlow.value = cameraManager.getCameraProvider()
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(cameraError = "Failed to initialize camera provider")
                }
            }
        }
    }

    fun onCameraError(error: String) {
        _uiState.value = _uiState.value.copy(cameraError = error)
    }

    companion object {
        private const val TAG = "CameraViewModel"
    }
}
