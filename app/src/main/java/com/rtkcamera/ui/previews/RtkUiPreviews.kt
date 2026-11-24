package com.rtkcamera.ui.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.rtkcamera.ui.ActiveSheet
import com.rtkcamera.ui.AlgorithmTuningState
import com.rtkcamera.ui.AppSettingsState
import com.rtkcamera.ui.AutomatedCaptureState
import com.rtkcamera.ui.CameraUiState
import com.rtkcamera.ui.CaptureMode
import com.rtkcamera.ui.CaptureState
import com.rtkcamera.ui.FlashMode
import com.rtkcamera.ui.FocusReticleState
import com.rtkcamera.ui.ProParametersState
import com.rtkcamera.ui.ScreenRoute
import com.rtkcamera.ui.TrackingOverlayState
import com.rtkcamera.ui.ViewfinderCallbacks
import com.rtkcamera.ui.ViewfinderContent
import com.rtkcamera.ui.theme.RtkCameraTheme

@Preview(name = "Hi-Fi 01a • Video Idle", widthDp = 393, heightDp = 852, showBackground = true)
@Composable
private fun PreviewHiFi01aVideoIdle() {
    RtkCameraTheme {
        ViewfinderContent(
            uiState = CameraUiState(
                captureMode = CaptureMode.VIDEO,
                captureState = CaptureState.Idle,
                flashMode = FlashMode.OFF
            ),
            callbacks = ViewfinderCallbacks()
        )
    }
}

@Preview(name = "Hi-Fi 01b • Video Recording", widthDp = 393, heightDp = 852, showBackground = true)
@Composable
private fun PreviewHiFi01bVideoRecording() {
    RtkCameraTheme {
        ViewfinderContent(
            uiState = CameraUiState(
                captureMode = CaptureMode.VIDEO,
                captureState = CaptureState.VideoRecording(isPaused = false, elapsedSeconds = 252L),
                trackingOverlay = TrackingOverlayState(visible = true, label = "Dog • 96% • 1.8m")
            ),
            callbacks = ViewfinderCallbacks()
        )
    }
}

@Preview(name = "Hi-Fi 01c • Photo Idle (Overrides + Reticle)", widthDp = 393, heightDp = 852, showBackground = true)
@Composable
private fun PreviewHiFi01cPhotoIdle() {
    RtkCameraTheme {
        ViewfinderContent(
            uiState = CameraUiState(
                captureMode = CaptureMode.PHOTO,
                captureState = CaptureState.Idle,
                flashMode = FlashMode.ON,
                focusReticle = FocusReticleState(visible = true, normalizedX = 0.5f, normalizedY = 0.45f),
                proParameters = ProParametersState(hasCustomOverrides = true),
                automatedCapture = AutomatedCaptureState(isArmed = true),
                appSettings = AppSettingsState(showRuleOfThirdsGrid = true)
            ),
            callbacks = ViewfinderCallbacks()
        )
    }
}

@Preview(name = "Hi-Fi 01d • Photo Capturing (Burst)", widthDp = 393, heightDp = 852, showBackground = true)
@Composable
private fun PreviewHiFi01dPhotoCapturing() {
    RtkCameraTheme {
        ViewfinderContent(
            uiState = CameraUiState(
                captureMode = CaptureMode.PHOTO,
                captureState = CaptureState.PhotoCapturing(burstCurrent = 2, burstTotal = 5)
            ),
            callbacks = ViewfinderCallbacks()
        )
    }
}

@Preview(name = "Hi-Fi 02 • Algorithm Tuning Drawer", widthDp = 393, heightDp = 852, showBackground = true)
@Composable
private fun PreviewHiFi02AlgorithmTuning() {
    RtkCameraTheme {
        ViewfinderContent(
            uiState = CameraUiState(
                activeSheet = ActiveSheet.ALGORITHM_TUNING
            ),
            callbacks = ViewfinderCallbacks()
        )
    }
}

@Preview(name = "Hi-Fi 03 • Performance Warning", widthDp = 393, heightDp = 852, showBackground = true)
@Composable
private fun PreviewHiFi03PerformanceWarning() {
    RtkCameraTheme {
        ViewfinderContent(
            uiState = CameraUiState(
                captureMode = CaptureMode.VIDEO,
                captureState = CaptureState.VideoRecording(isPaused = false, elapsedSeconds = 108L),
                telemetryFps = 17f,
                telemetryLatencyMs = 58.4f,
                frameDropPercent = 43,
                algorithmTuning = AlgorithmTuningState(
                    selectedModuleId = "depth",
                    selectedModuleName = "Depth-Net"
                ),
                performanceWarning = "Low FPS Warning: 17 fps (58 ms)"
            ),
            callbacks = ViewfinderCallbacks()
        )
    }
}

@Preview(name = "Hi-Fi 04 • Snapshot Inspector", widthDp = 393, heightDp = 852, showBackground = true)
@Composable
private fun PreviewHiFi04SnapshotInspector() {
    RtkCameraTheme {
        ViewfinderContent(
            uiState = CameraUiState(
                currentRoute = ScreenRoute.SNAPSHOT_INSPECTOR
            ),
            callbacks = ViewfinderCallbacks()
        )
    }
}

@Preview(name = "Hi-Fi 05 • Pro Camera Parameters", widthDp = 393, heightDp = 852, showBackground = true)
@Composable
private fun PreviewHiFi05ProParameters() {
    RtkCameraTheme {
        ViewfinderContent(
            uiState = CameraUiState(
                activeSheet = ActiveSheet.PRO_PARAMETERS
            ),
            callbacks = ViewfinderCallbacks()
        )
    }
}

@Preview(name = "Hi-Fi 06 • Automated Capture & Intervalometer", widthDp = 393, heightDp = 852, showBackground = true)
@Composable
private fun PreviewHiFi06AutomatedCapture() {
    RtkCameraTheme {
        ViewfinderContent(
            uiState = CameraUiState(
                activeSheet = ActiveSheet.AUTOMATED_CAPTURE
            ),
            callbacks = ViewfinderCallbacks()
        )
    }
}

@Preview(name = "Hi-Fi 07 • Field & RTK Settings", widthDp = 393, heightDp = 852, showBackground = true)
@Composable
private fun PreviewHiFi07Settings() {
    RtkCameraTheme {
        ViewfinderContent(
            uiState = CameraUiState(
                activeSheet = ActiveSheet.SETTINGS
            ),
            callbacks = ViewfinderCallbacks()
        )
    }
}
