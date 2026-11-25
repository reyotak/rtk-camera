package com.rtkcamera.ui

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rtkcamera.ui.components.AiTrackingOverlay
import com.rtkcamera.ui.components.AlgorithmTuningSheet
import com.rtkcamera.ui.components.AspectRatioFramingOverlay
import com.rtkcamera.ui.components.AutomatedCaptureSheet
import com.rtkcamera.ui.components.BottomControlDeck
import com.rtkcamera.ui.components.BottomViewfinderScrim
import com.rtkcamera.ui.components.CountdownOverlay
import com.rtkcamera.ui.components.FocusReticleOverlay
import com.rtkcamera.ui.components.HelpSheet
import com.rtkcamera.ui.components.ProParametersSheet
import com.rtkcamera.ui.components.RuleOfThirdsGridOverlay
import com.rtkcamera.ui.components.ShutterFlashOverlay
import com.rtkcamera.ui.components.TopPreviewOverlay
import com.rtkcamera.ui.components.TopViewfinderScrim
import com.rtkcamera.ui.components.ViewfinderBottomOverlayBar
import com.rtkcamera.ui.theme.RtkColorTokens

/**
 * Callback bundle for stateless ViewfinderContent (enables Previews and tests).
 */
data class ViewfinderCallbacks(
    val onCycleFlashMode: () -> Unit = {},
    val onHelpClick: () -> Unit = {},
    val onSelectCaptureMode: (CaptureMode) -> Unit = {},
    val onSelectZoom: (ZoomLevel) -> Unit = {},
    val onViewfinderTap: (Float, Float) -> Unit = { _, _ -> },
    val onShutterClick: () -> Unit = {},
    val onPauseRecordingClick: () -> Unit = {},
    val onOpenSheet: (ActiveSheet) -> Unit = {},
    val onDismissSheet: () -> Unit = {},
    val onUpdateAlgorithmTuning: ((AlgorithmTuningState) -> AlgorithmTuningState) -> Unit = {},
    val onResetAlgorithmTuning: () -> Unit = {},
    val onApplyAlgorithmTuning: () -> Unit = {},
    val onUpdateProParameters: ((ProParametersState) -> ProParametersState) -> Unit = {},
    val onResetProParameters: () -> Unit = {},
    val onApplyProParameters: () -> Unit = {},
    val onUpdateAutomatedCapture: ((AutomatedCaptureState) -> AutomatedCaptureState) -> Unit = {},
    val onResetAutomatedCapture: () -> Unit = {},
    val onArmAutomatedCapture: () -> Unit = {},
    val onUpdateSettings: ((AppSettingsState) -> AppSettingsState) -> Unit = {},
    val onDismissPerformanceWarning: () -> Unit = {},
    val onOptimizeParametersClick: () -> Unit = {},
    val onOpenSnapshotInspector: () -> Unit = {},
    val onCloseSnapshotInspector: () -> Unit = {},
    val onCameraSwitchClick: () -> Unit = {}
)

/**
 * Stateful Main Viewfinder screen bound to CameraViewModel and CameraX PreviewView.
 */
@Composable
fun ViewfinderScreen(
    viewModel: CameraViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cameraProvider by viewModel.cameraProviderFlow.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.onPermissionResult(isGranted)
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    val callbacks = ViewfinderCallbacks(
        onCycleFlashMode = viewModel::onCycleFlashMode,
        onHelpClick = { viewModel.onOpenSheet(ActiveSheet.HELP) },
        onSelectCaptureMode = viewModel::onSelectCaptureMode,
        onSelectZoom = viewModel::onSelectZoom,
        onViewfinderTap = viewModel::onViewfinderTap,
        onShutterClick = viewModel::onCaptureTriggered,
        onPauseRecordingClick = viewModel::onTogglePauseRecording,
        onOpenSheet = viewModel::onOpenSheet,
        onDismissSheet = viewModel::onDismissSheet,
        onUpdateAlgorithmTuning = viewModel::onUpdateAlgorithmTuning,
        onResetAlgorithmTuning = viewModel::onResetAlgorithmTuning,
        onApplyAlgorithmTuning = viewModel::onApplyAlgorithmTuning,
        onUpdateProParameters = viewModel::onUpdateProParameters,
        onResetProParameters = viewModel::onResetProParameters,
        onApplyProParameters = viewModel::onApplyProParameters,
        onUpdateAutomatedCapture = viewModel::onUpdateAutomatedCapture,
        onResetAutomatedCapture = viewModel::onResetAutomatedCapture,
        onArmAutomatedCapture = viewModel::onArmAutomatedCapture,
        onUpdateSettings = viewModel::onUpdateSettings,
        onDismissPerformanceWarning = viewModel::onDismissPerformanceWarning,
        onOptimizeParametersClick = viewModel::onOptimizeParametersClicked,
        onOpenSnapshotInspector = viewModel::onOpenSnapshotInspector,
        onCloseSnapshotInspector = viewModel::onCloseSnapshotInspector,
        onCameraSwitchClick = { /* UI-only placeholder */ }
    )

    ViewfinderContent(
        uiState = uiState,
        callbacks = callbacks,
        modifier = modifier,
        previewContent = {
            if (uiState.hasCameraPermission) {
                if (cameraProvider != null) {
                    AndroidView(
                        factory = { ctx ->
                            PreviewView(ctx).apply {
                                scaleType = PreviewView.ScaleType.FILL_CENTER
                                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                            }
                        },
                        modifier = Modifier.fillMaxSize(),
                        update = { previewView ->
                            val preview = Preview.Builder().build().also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }
                            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                            try {
                                cameraProvider?.unbindAll()
                                cameraProvider?.bindToLifecycle(
                                    lifecycleOwner,
                                    cameraSelector,
                                    preview
                                )
                            } catch (exc: Exception) {
                                Log.e("ViewfinderScreen", "Use case binding failed", exc)
                                viewModel.onCameraError("Binding failed")
                            }
                        }
                    )
                } else if (uiState.cameraError == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = RtkColorTokens.AccentCyan)
                    }
                }
            }
        }
    )
}

/**
 * Stateless Viewfinder & Modal Sheet host implementing Penpot Hi-Fi 01a–07.
 */
@Composable
fun ViewfinderContent(
    uiState: CameraUiState,
    callbacks: ViewfinderCallbacks,
    modifier: Modifier = Modifier,
    previewContent: @Composable BoxScope.() -> Unit = {}
) {
    if (uiState.currentRoute == ScreenRoute.SNAPSHOT_INSPECTOR) {
        SnapshotInspectorScreen(
            metadata = uiState.snapshotMetadata,
            onBackClick = callbacks.onCloseSnapshotInspector,
            modifier = modifier
        )
        return
    }

    val isWarningVisible = uiState.performanceWarning != null && !uiState.isPerformanceWarningDismissed
    val showZoomBar = uiState.captureState is CaptureState.Idle || uiState.captureState is CaptureState.VideoRecording

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(RtkColorTokens.CanvasDeep)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Edge-to-Edge Camera Viewport reaching the very top of the screen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(RtkColorTokens.CanvasDeep)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            if (size.width > 0 && size.height > 0) {
                                callbacks.onViewfinderTap(
                                    offset.x / size.width.toFloat(),
                                    offset.y / size.height.toFloat()
                                )
                            }
                        }
                    }
            ) {
                // Live CameraX PreviewView (or Preview placeholder)
                previewContent()

                // Composition & Framing Overlays
                if (uiState.appSettings.showAspectFramingGuides) {
                    AspectRatioFramingOverlay(preset = uiState.proParameters.aspectResolution)
                }
                if (uiState.appSettings.showRuleOfThirdsGrid) {
                    RuleOfThirdsGridOverlay()
                }

                // Soft Top & Bottom Scrims for Outdoor Legibility
                TopViewfinderScrim(modifier = Modifier.align(Alignment.TopCenter))
                if (showZoomBar) {
                    BottomViewfinderScrim(modifier = Modifier.align(Alignment.BottomCenter))
                }

                // Tap-to-Focus Reticle Overlay
                FocusReticleOverlay(focusReticle = uiState.focusReticle)

                // Shutter Optical Flash Overlay (Hi-Fi 01d)
                androidx.compose.animation.AnimatedVisibility(
                    visible = uiState.captureState is CaptureState.PhotoCapturing,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    ShutterFlashOverlay()
                }

                // AI Tracking Overlay (Hi-Fi 01b)
                if (uiState.trackingOverlay.visible) {
                    AiTrackingOverlay(
                        trackingState = uiState.trackingOverlay,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Countdown Overlay (3… 2… 1…)
                val countdownState = uiState.captureState as? CaptureState.Countdown
                if (countdownState != null) {
                    CountdownOverlay(secondsRemaining = countdownState.secondsRemaining)
                }

                // Floating Top Preview Overlay (FPS pill / Active Badges / REC timer / Warning Card)
                TopPreviewOverlay(
                    captureState = uiState.captureState,
                    fps = uiState.telemetryFps,
                    latencyMs = uiState.telemetryLatencyMs,
                    frameDropPercent = uiState.frameDropPercent,
                    algorithmName = uiState.algorithmTuning.selectedModuleName,
                    showTelemetryHud = uiState.appSettings.showTelemetryHud,
                    proSummaryBadge = uiState.proParameters.summaryBadge,
                    armedSummaryBadge = uiState.automatedCapture.armedSummaryBadge,
                    isPerformanceWarningVisible = isWarningVisible,
                    onOptimizeClick = callbacks.onOptimizeParametersClick,
                    onDismissWarningClick = callbacks.onDismissPerformanceWarning,
                    modifier = Modifier.align(Alignment.TopCenter)
                )

                // Camera Error Banner (if permission denied or binding error)
                uiState.cameraError?.let { errorMsg ->
                    Surface(
                        color = Color.Black.copy(alpha = 0.72f),
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp)
                    ) {
                        Text(
                            text = errorMsg,
                            color = RtkColorTokens.TextPrimary,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        )
                    }
                }

                // Bottom Viewfinder Bar: Camera Switch (left) | Zoom Bar (center) | Flash (right)
                // Keeps Zoom accessible during Video Recording while hiding side controls
                if (showZoomBar) {
                    ViewfinderBottomOverlayBar(
                        selectedZoom = uiState.selectedZoom,
                        flashMode = uiState.flashMode,
                        showSideControls = uiState.captureState is CaptureState.Idle,
                        onCameraSwitchClick = callbacks.onCameraSwitchClick,
                        onSelectZoom = callbacks.onSelectZoom,
                        onCycleFlashMode = callbacks.onCycleFlashMode,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }

            // Ergonomic Bottom Control Deck
            BottomControlDeck(
                captureMode = uiState.captureMode,
                captureState = uiState.captureState,
                algorithmPillLabel = uiState.activeAlgorithmPillLabel,
                hasProOverrides = uiState.proParameters.hasCustomOverrides,
                isAutomationArmed = uiState.automatedCapture.isArmed,
                onShutterClick = callbacks.onShutterClick,
                onPauseRecordingClick = callbacks.onPauseRecordingClick,
                onProParametersClick = { callbacks.onOpenSheet(ActiveSheet.PRO_PARAMETERS) },
                onAutomatedCaptureClick = { callbacks.onOpenSheet(ActiveSheet.AUTOMATED_CAPTURE) },
                onGalleryClick = callbacks.onOpenSnapshotInspector,
                onAlgorithmPillClick = { callbacks.onOpenSheet(ActiveSheet.ALGORITHM_TUNING) },
                onHelpClick = callbacks.onHelpClick,
                onSelectCaptureMode = callbacks.onSelectCaptureMode
            )
        }

        // Modal Bottom Sheets (Hi-Fi 02, 05, 06, 07)
        when (uiState.activeSheet) {
            ActiveSheet.NONE -> Unit

            ActiveSheet.ALGORITHM_TUNING -> {
                AlgorithmTuningSheet(
                    state = uiState.algorithmTuning,
                    availableAlgorithms = uiState.availableAlgorithms,
                    onUpdateState = callbacks.onUpdateAlgorithmTuning,
                    onReset = callbacks.onResetAlgorithmTuning,
                    onApply = callbacks.onApplyAlgorithmTuning,
                    onDismiss = callbacks.onDismissSheet
                )
            }

            ActiveSheet.PRO_PARAMETERS -> {
                ProParametersSheet(
                    state = uiState.proParameters,
                    onUpdateState = callbacks.onUpdateProParameters,
                    onReset = callbacks.onResetProParameters,
                    onApply = callbacks.onApplyProParameters,
                    onDismiss = callbacks.onDismissSheet
                )
            }

            ActiveSheet.AUTOMATED_CAPTURE -> {
                AutomatedCaptureSheet(
                    state = uiState.automatedCapture,
                    onUpdateState = callbacks.onUpdateAutomatedCapture,
                    onReset = callbacks.onResetAutomatedCapture,
                    onArm = callbacks.onArmAutomatedCapture,
                    onDismiss = callbacks.onDismissSheet
                )
            }

            ActiveSheet.HELP -> {
                HelpSheet(
                    state = uiState.appSettings,
                    onUpdateState = callbacks.onUpdateSettings,
                    onDismiss = callbacks.onDismissSheet
                )
            }
        }
    }
}
