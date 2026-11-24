package com.rtkcamera.ui

import android.util.Log
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rtkcamera.camera.AnalysisPipeline
import com.rtkcamera.camera.CameraManager
import com.rtkcamera.nativebridge.AlgorithmInfo
import com.rtkcamera.nativebridge.NativeLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI State for the RTK-Camera Screen and all Penpot Hi-Fi states (01a–07).
 */
data class CameraUiState(
    val hasCameraPermission: Boolean = false,
    val cameraError: String? = null,
    val activeAlgorithmId: String? = null,
    val availableAlgorithms: List<AlgorithmInfo> = emptyList(),
    val isProcessing: Boolean = false,
    val lastProcessingTimeMs: Long = 0,
    val performanceWarning: String? = null, // Displayed if processing > 33ms (Principle III)
    // Penpot HUD & Modal UI States
    val currentRoute: ScreenRoute = ScreenRoute.VIEWFINDER,
    val captureMode: CaptureMode = CaptureMode.PHOTO,
    val captureState: CaptureState = CaptureState.Idle,
    val activeSheet: ActiveSheet = ActiveSheet.NONE,
    val flashMode: FlashMode = FlashMode.OFF,
    val selectedZoom: ZoomLevel = ZoomLevel.WIDE,
    val telemetryFps: Float = 30.0f,
    val telemetryLatencyMs: Float = 16.4f,
    val frameDropPercent: Int = 43,
    val isPerformanceWarningDismissed: Boolean = false,
    val focusReticle: FocusReticleState = FocusReticleState(),
    val trackingOverlay: TrackingOverlayState = TrackingOverlayState(),
    val algorithmTuning: AlgorithmTuningState = AlgorithmTuningState(),
    val proParameters: ProParametersState = ProParametersState(),
    val automatedCapture: AutomatedCaptureState = AutomatedCaptureState(),
    val appSettings: AppSettingsState = AppSettingsState(),
    val snapshotMetadata: SnapshotMetadata = SnapshotMetadata()
) {
    val activeAlgorithmPillLabel: String
        get() {
            val moduleName = algorithmTuning.selectedModuleName
            return when (captureMode) {
                CaptureMode.VIDEO -> {
                    val norm = algorithmTuning.lowThreshold / 255f
                    if (moduleName.contains("Canny", ignoreCase = true)) {
                        String.format("Canny-Edge v2.1 (%.2f)", norm.coerceAtLeast(0.01f))
                    } else {
                        moduleName
                    }
                }
                CaptureMode.PHOTO -> {
                    if (moduleName.contains("Canny", ignoreCase = true)) {
                        "Canny-Edge (Capture FX)"
                    } else {
                        "$moduleName (Capture FX)"
                    }
                }
            }
        }
}

/**
 * ViewModel for managing camera UI state and pure UI-state transitions.
 */
@HiltViewModel
class CameraViewModel @Inject constructor(
    private val nativeLoader: NativeLoader,
    private val analysisPipeline: AnalysisPipeline,
    private val cameraManager: CameraManager
) : ViewModel() {

    private val _cameraProviderFlow = MutableStateFlow<ProcessCameraProvider?>(null)
    val cameraProviderFlow = _cameraProviderFlow.asStateFlow()

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState = _uiState.asStateFlow()

    private var recordingTimerJob: Job? = null
    private var photoCaptureJob: Job? = null
    private var focusReticleJob: Job? = null

    init {
        // Background loop to monitor performance metrics
        viewModelScope.launch {
            while (true) {
                val lastTime = analysisPipeline.getLastProcessingTimeMs()
                _uiState.update { current ->
                    if (current.appSettings.simulatePerformanceWarning) {
                        current
                    } else {
                        val warning = if (lastTime > 33) {
                            val fps = if (lastTime > 0) 1000 / lastTime else 0
                            "Low FPS Warning: $fps fps ($lastTime ms)"
                        } else {
                            null
                        }
                        val effectiveLatency = if (lastTime > 0) lastTime.toFloat() else current.telemetryLatencyMs
                        val effectiveFps = if (lastTime > 0) (1000f / lastTime).coerceAtMost(60f) else current.telemetryFps
                        current.copy(
                            lastProcessingTimeMs = lastTime,
                            telemetryLatencyMs = effectiveLatency,
                            telemetryFps = effectiveFps,
                            performanceWarning = warning,
                            isPerformanceWarningDismissed = if (warning == null) false else current.isPerformanceWarningDismissed
                        )
                    }
                }
                delay(500)
            }
        }
    }

    /**
     * Handles algorithm selection from the UI.
     */
    fun onAlgorithmSelected(algorithm: AlgorithmInfo) {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true) }
            val success = nativeLoader.load(algorithm)
            _uiState.update {
                it.copy(
                    activeAlgorithmId = if (success) algorithm.id else null,
                    isProcessing = false
                )
            }
        }
    }

    /**
     * Handles manual capture trigger (pure UI state transition).
     */
    fun onCaptureTriggered() {
        Log.d(TAG, "Capture triggered (UI state transition)")
        when (_uiState.value.captureMode) {
            CaptureMode.VIDEO -> toggleVideoRecordingUiState()
            CaptureMode.PHOTO -> triggerPhotoCaptureUiState()
        }
    }

    fun onToggleCaptureMode() {
        _uiState.update(CameraUiTransitions::toggleCaptureMode)
    }

    fun onSelectCaptureMode(mode: CaptureMode) {
        _uiState.update { CameraUiTransitions.selectCaptureMode(it, mode) }
    }

    fun onCycleFlashMode() {
        _uiState.update(CameraUiTransitions::cycleFlashMode)
    }

    fun onSelectZoom(zoom: ZoomLevel) {
        _uiState.update { CameraUiTransitions.selectZoom(it, zoom) }
    }

    fun onViewfinderTap(normalizedX: Float, normalizedY: Float) {
        if (_uiState.value.activeSheet != ActiveSheet.NONE) return
        _uiState.update { CameraUiTransitions.showFocusReticle(it, normalizedX, normalizedY) }
        focusReticleJob?.cancel()
        focusReticleJob = viewModelScope.launch {
            delay(2200L)
            _uiState.update(CameraUiTransitions::hideFocusReticle)
        }
    }

    fun onTogglePauseRecording() {
        _uiState.update(CameraUiTransitions::togglePauseVideoRecording)
    }

    fun onOpenSheet(sheet: ActiveSheet) {
        _uiState.update { CameraUiTransitions.openSheet(it, sheet) }
    }

    fun onDismissSheet() {
        _uiState.update(CameraUiTransitions::dismissSheet)
    }

    fun onUpdateAlgorithmTuning(transform: (AlgorithmTuningState) -> AlgorithmTuningState) {
        _uiState.update { CameraUiTransitions.updateAlgorithmTuning(it, transform) }
    }

    fun onResetAlgorithmTuning() {
        _uiState.update(CameraUiTransitions::resetAlgorithmTuning)
    }

    fun onApplyAlgorithmTuning() {
        _uiState.update(CameraUiTransitions::dismissSheet)
    }

    fun onUpdateProParameters(transform: (ProParametersState) -> ProParametersState) {
        _uiState.update { CameraUiTransitions.updateProParameters(it, transform) }
    }

    fun onResetProParameters() {
        _uiState.update(CameraUiTransitions::resetProParameters)
    }

    fun onApplyProParameters() {
        _uiState.update(CameraUiTransitions::dismissSheet)
    }

    fun onUpdateAutomatedCapture(transform: (AutomatedCaptureState) -> AutomatedCaptureState) {
        _uiState.update { CameraUiTransitions.updateAutomatedCapture(it, transform) }
    }

    fun onResetAutomatedCapture() {
        _uiState.update(CameraUiTransitions::resetAutomatedCapture)
    }

    fun onArmAutomatedCapture() {
        _uiState.update(CameraUiTransitions::armAutomatedCapture)
    }

    fun onUpdateSettings(transform: (AppSettingsState) -> AppSettingsState) {
        _uiState.update { CameraUiTransitions.updateSettings(it, transform) }
    }

    fun onDismissPerformanceWarning() {
        _uiState.update(CameraUiTransitions::dismissPerformanceWarning)
    }

    fun onOptimizeParametersClicked() {
        _uiState.update(CameraUiTransitions::openOptimizeFromWarning)
    }

    fun onOpenSnapshotInspector() {
        _uiState.update(CameraUiTransitions::openSnapshotInspector)
    }

    fun onCloseSnapshotInspector() {
        _uiState.update(CameraUiTransitions::closeSnapshotInspector)
    }

    fun onPermissionResult(granted: Boolean) {
        _uiState.update {
            it.copy(
                hasCameraPermission = granted,
                cameraError = if (granted) null else "Camera permission denied"
            )
        }
        if (granted) {
            viewModelScope.launch {
                try {
                    _cameraProviderFlow.value = cameraManager.getCameraProvider()
                } catch (e: Exception) {
                    _uiState.update { it.copy(cameraError = "Failed to initialize camera provider") }
                }
            }
        }
    }

    fun onCameraError(error: String) {
        _uiState.update { it.copy(cameraError = error) }
    }

    private fun toggleVideoRecordingUiState() {
        val current = _uiState.value
        val wasRecording = current.captureState is CaptureState.VideoRecording
        recordingTimerJob?.cancel()
        photoCaptureJob?.cancel()

        if (wasRecording) {
            _uiState.update(CameraUiTransitions::startOrStopVideoRecording)
            return
        }

        val countdownSec = if (current.automatedCapture.isArmed) current.automatedCapture.countdownTimer.seconds else 0
        recordingTimerJob = viewModelScope.launch {
            if (countdownSec > 0) {
                for (sec in countdownSec downTo 1) {
                    _uiState.update { CameraUiTransitions.updateCountdown(it, sec) }
                    delay(1000L)
                }
            }
            _uiState.update(CameraUiTransitions::startOrStopVideoRecording)
            while (true) {
                delay(1000L)
                _uiState.update(CameraUiTransitions::tickRecordingTimer)
            }
        }
    }

    private fun triggerPhotoCaptureUiState() {
        val current = _uiState.value
        if (current.captureState is CaptureState.PhotoCapturing || current.captureState is CaptureState.Countdown) return
        photoCaptureJob?.cancel()

        val armed = current.automatedCapture.isArmed
        val countdownSec = if (armed) current.automatedCapture.countdownTimer.seconds else 0
        val totalFrames = if (armed) current.automatedCapture.burstCount.count else 1
        val intervalMs = if (armed) {
            (current.automatedCapture.intervalDelay.seconds * 1000L).toLong().coerceIn(250L, 1200L)
        } else {
            700L
        }

        photoCaptureJob = viewModelScope.launch {
            if (countdownSec > 0) {
                for (sec in countdownSec downTo 1) {
                    _uiState.update { CameraUiTransitions.updateCountdown(it, sec) }
                    delay(1000L)
                }
            }
            for (frame in 1..totalFrames) {
                _uiState.update { CameraUiTransitions.beginPhotoCapture(it, frame, totalFrames) }
                delay(intervalMs)
            }
            _uiState.update(CameraUiTransitions::finishPhotoCapture)
        }
    }

    companion object {
        private const val TAG = "CameraViewModel"
    }
}
