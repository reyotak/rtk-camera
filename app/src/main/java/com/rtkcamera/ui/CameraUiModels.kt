package com.rtkcamera.ui

import kotlin.math.roundToInt

/**
 * Top-level screen routes within the RTK-Camera UI.
 */
enum class ScreenRoute {
    VIEWFINDER,
    SNAPSHOT_INSPECTOR
}

/**
 * Primary viewfinder capture modes (Hi-Fi 01a/01b vs Hi-Fi 01c/01d).
 */
enum class CaptureMode {
    VIDEO,
    PHOTO
}

/**
 * Explicit 3-state Flash mode so Off/On/Auto never rely solely on color.
 */
enum class FlashMode(val label: String) {
    OFF("Flash Off"),
    ON("Flash On"),
    AUTO("Flash Auto");

    fun next(): FlashMode = when (this) {
        OFF -> ON
        ON -> AUTO
        AUTO -> OFF
    }
}

/**
 * Active capture lifecycle state rendered by the viewfinder HUD.
 */
sealed interface CaptureState {
    data object Idle : CaptureState
    data class Countdown(val secondsRemaining: Int) : CaptureState
    data class VideoRecording(
        val isPaused: Boolean = false,
        val elapsedSeconds: Long = 0L
    ) : CaptureState
    data class PhotoCapturing(
        val burstCurrent: Int = 1,
        val burstTotal: Int = 1
    ) : CaptureState
}

/**
 * Modal bottom sheets available from the viewfinder HUD (Hi-Fi 02, 05, 06, 07).
 */
enum class ActiveSheet {
    NONE,
    ALGORITHM_TUNING,
    PRO_PARAMETERS,
    AUTOMATED_CAPTURE,
    HELP
}

/**
 * Sub-sections navigable inside the Help & Info bottom sheet.
 */
enum class HelpSection {
    MENU,
    TUTORIAL,
    CONTROLS_GUIDE,
    ABOUT
}

/**
 * Optical / digital zoom presets shown in the circular Zoom Bar.
 */
enum class ZoomLevel(val label: String) {
    ULTRA_WIDE(".5"),
    WIDE("1x"),
    TELE("2")
}

/**
 * Gaussian blur kernel sizes in Hi-Fi 02 Algorithm Tuning Drawer.
 */
enum class GaussianKernelSize(val label: String) {
    KERNEL_3X3("3x3 (Fast)"),
    KERNEL_5X5("5x5 (Smooth)"),
    KERNEL_7X7("7x7 (Heavy)")
}

/**
 * UI state for Hi-Fi 02 • Algorithm Sandbox & Tuning Drawer.
 */
data class AlgorithmTuningState(
    val selectedModuleId: String = "canny_v2_1",
    val selectedModuleName: String = "Canny v2.1",
    val lowThreshold: Int = 45,
    val highThreshold: Int = 140,
    val kernelSize: GaussianKernelSize = GaussianKernelSize.KERNEL_3X3,
    val saveRawWithSnapshot: Boolean = true
) {
    companion object {
        val DefaultModules = listOf(
            "raw" to "Raw",
            "sobel_2d" to "Sobel 2D",
            "canny_v2_1" to "Canny v2.1",
            "depth" to "Depth"
        )
    }
}

/**
 * UI state for Hi-Fi 05 • Pro Camera Parameters Sheet.
 */
enum class AspectResolutionPreset(val label: String, val ratioValue: Float) {
    RATIO_4_3_1080P("4:3 • 1080p", 3f / 4f),
    RATIO_16_9_4K("16:9 • 4K", 9f / 16f),
    RATIO_4_3_12MP("4:3 • 12MP", 3f / 4f),
    RATIO_1_1_FHD("1:1 • 1080p", 1f)
}

enum class BracketFrame(val label: String) {
    FRAME_1("Frame 1: -2 EV"),
    FRAME_2("Frame 2: 0 EV"),
    FRAME_3("Frame 3: +2 EV")
}

enum class WhiteBalancePreset(val label: String) {
    AWB("AWB"),
    DAYLIGHT("5500K"),
    CLOUDY("6500K"),
    TUNGSTEN("3200K")
}

enum class ShutterSpeedPreset(val label: String) {
    S_1_1000("1/1000s"),
    S_1_500("1/500s"),
    S_1_250("1/250s"),
    S_1_125("1/125s"),
    S_1_60("1/60s")
}

enum class IsoPreset(val label: String) {
    AUTO("Auto"),
    ISO_100("100"),
    ISO_200("200"),
    ISO_400("400"),
    ISO_800("800")
}

data class ProParametersState(
    val aspectResolution: AspectResolutionPreset = AspectResolutionPreset.RATIO_4_3_1080P,
    val selectedBracket: BracketFrame = BracketFrame.FRAME_2,
    val whiteBalance: WhiteBalancePreset = WhiteBalancePreset.AWB,
    val manualFocusProgress: Float = 0.5f,
    val shutterSpeed: ShutterSpeedPreset = ShutterSpeedPreset.S_1_250,
    val iso: IsoPreset = IsoPreset.AUTO,
    val hasCustomOverrides: Boolean = false
) {
    val focusDistanceLabel: String
        get() = when {
            manualFocusProgress >= 0.98f -> "∞ (Infinity)"
            manualFocusProgress <= 0.02f -> "0.1m (Macro)"
            else -> {
                val meters = 0.1f + manualFocusProgress * 2.65f
                String.format("%.1fm (Locked)", meters)
            }
        }

    val summaryBadge: String?
        get() = if (!hasCustomOverrides) {
            null
        } else {
            val isoText = if (iso == IsoPreset.AUTO) "ISO Auto" else "ISO ${iso.label}"
            "${aspectResolution.label} • ${shutterSpeed.label} • $isoText"
        }
}

/**
 * UI state for Hi-Fi 06 • Automated Capture & Intervalometer Sheet.
 */
enum class BurstCountOption(val count: Int, val chipLabel: String) {
    SINGLE(1, "1 (Single)"),
    THREE(3, "3 Frames"),
    FIVE(5, "5 Frames"),
    TEN(10, "10 Frames"),
    TWENTY_FIVE(25, "25")
}

enum class IntervalDelayOption(val seconds: Float, val chipLabel: String, val descriptor: String) {
    S_0_1(0.1f, "0.1s", "0.1s (Ultra Burst)"),
    S_0_5(0.5f, "0.5s", "0.5s (High-Speed Burst)"),
    S_1_0(1.0f, "1.0s", "1.0s (Standard)"),
    S_2_0(2.0f, "2.0s", "2.0s (Timelapse)"),
    S_5_0(5.0f, "5.0s", "5.0s (Interval)"),
    S_10_0(10.0f, "10s", "10s (Long Interval)")
}

enum class MaxVideoDurationOption(val chipLabel: String) {
    UNLIMITED("Unlimited"),
    CLIP_15S("15s Clip"),
    CLIP_30S("30s"),
    CLIP_60S("60s"),
    CLIP_5M("5 Min")
}

enum class CountdownTimerOption(val seconds: Int, val chipLabel: String) {
    INSTANT(0, "Instant (0s)"),
    DELAY_3S(3, "3s Delay"),
    DELAY_10S(10, "10s Delay")
}

data class AutomatedCaptureState(
    val burstCount: BurstCountOption = BurstCountOption.FIVE,
    val intervalDelay: IntervalDelayOption = IntervalDelayOption.S_0_5,
    val maxVideoDuration: MaxVideoDurationOption = MaxVideoDurationOption.CLIP_30S,
    val countdownTimer: CountdownTimerOption = CountdownTimerOption.INSTANT,
    val isArmed: Boolean = false
) {
    val burstSummaryLabel: String
        get() = if (burstCount.count == 1) "1 Frame Selected" else "${burstCount.count} Frames Selected"

    val armedSummaryBadge: String?
        get() = if (!isArmed) {
            null
        } else {
            val timerSuffix = if (countdownTimer.seconds > 0) " • ${countdownTimer.seconds}s" else ""
            "ARMED • ${burstCount.count}x @ ${intervalDelay.chipLabel}$timerSuffix"
        }

    val estimatedSessionText: String
        get() {
            val totalSeconds = burstCount.count * intervalDelay.seconds
            val formattedTotal = if (totalSeconds % 1f == 0f) {
                "${totalSeconds.roundToInt()}s"
            } else {
                String.format("%.1fs", totalSeconds)
            }
            return "Estimated Session: ${burstCount.count} Burst Frames @ ${intervalDelay.chipLabel} interval = ~$formattedTotal total capture time.\nAuto-stop buffer protection enabled."
        }
}

/**
 * UI state for Hi-Fi 07 • Help, Tutorial, About & HUD Diagnostics Sheet.
 */
data class AppSettingsState(
    val activeHelpSection: HelpSection = HelpSection.MENU,
    val showRuleOfThirdsGrid: Boolean = false,
    val showAspectFramingGuides: Boolean = false,
    val showTelemetryHud: Boolean = true,
    val showDemoTrackingOverlay: Boolean = false,
    val simulatePerformanceWarning: Boolean = false,
    val rtkStatusText: String = "RTK FIXED • 1.2cm • 18 Sats"
)

/**
 * Tap-to-focus reticle overlay state on the viewfinder.
 */
data class FocusReticleState(
    val visible: Boolean = false,
    val normalizedX: Float = 0.5f,
    val normalizedY: Float = 0.5f
)

/**
 * Optional AI tracking bounding box overlay shown in Hi-Fi 01b.
 */
data class TrackingOverlayState(
    val visible: Boolean = false,
    val label: String = "Dog • 96% • 1.8m"
)

/**
 * Metadata displayed in Hi-Fi 04 • High-Res Snapshot Review.
 */
data class SnapshotMetadata(
    val savedBannerText: String = "12.2 MP Processed Snapshot Saved to Gallery",
    val fileName: String = "RTK_20261020_094122_CannyV2.jpg",
    val subtitle: String = "Full-Resolution Processed Snapshot • JPEG + XMP Geotag",
    val resolutionValue: String = "4032 x 3024 (12.2 MP)",
    val resolutionSubtext: String = "Sensor Full Native",
    val algorithmValue: String = "Canny Edge v2.1",
    val algorithmSubtext: String = "Compute: 142ms",
    val exposureValue: String = "1/250s • ISO 100",
    val exposureSubtext: String = "f/1.8 • 24mm equivalent",
    val storageValue: String = "4.8 MB (JPEG + DNG)",
    val storageSubtext: String = "sRGB Color Profile"
)

/**
 * Formats recording elapsed seconds as HH:MM:SS (e.g., 00:04:12).
 */
fun formatRecordingTimer(elapsedSeconds: Long): String {
    val safeSeconds = elapsedSeconds.coerceAtLeast(0L)
    val hours = safeSeconds / 3600
    val minutes = (safeSeconds % 3600) / 60
    val seconds = safeSeconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

/**
 * Pure UI state transition helpers (no side effects or Android framework dependencies).
 */
object CameraUiTransitions {

    fun toggleCaptureMode(state: CameraUiState): CameraUiState {
        if (state.captureState !is CaptureState.Idle) return state
        val nextMode = when (state.captureMode) {
            CaptureMode.VIDEO -> CaptureMode.PHOTO
            CaptureMode.PHOTO -> CaptureMode.VIDEO
        }
        return state.copy(captureMode = nextMode)
    }

    fun selectCaptureMode(state: CameraUiState, mode: CaptureMode): CameraUiState {
        if (state.captureState !is CaptureState.Idle) return state
        return state.copy(captureMode = mode)
    }

    fun cycleFlashMode(state: CameraUiState): CameraUiState =
        state.copy(flashMode = state.flashMode.next())

    fun selectZoom(state: CameraUiState, zoom: ZoomLevel): CameraUiState =
        state.copy(selectedZoom = zoom)

    fun openSheet(state: CameraUiState, sheet: ActiveSheet): CameraUiState {
        if (state.captureState !is CaptureState.Idle && sheet != ActiveSheet.ALGORITHM_TUNING) {
            return state
        }
        return state.copy(activeSheet = sheet)
    }

    fun dismissSheet(state: CameraUiState): CameraUiState =
        state.copy(
            activeSheet = ActiveSheet.NONE,
            appSettings = state.appSettings.copy(activeHelpSection = HelpSection.MENU)
        )

    fun startOrStopVideoRecording(state: CameraUiState): CameraUiState =
        when (state.captureState) {
            is CaptureState.Idle, is CaptureState.Countdown -> state.copy(
                captureState = CaptureState.VideoRecording(isPaused = false, elapsedSeconds = 0L),
                activeSheet = ActiveSheet.NONE
            )
            is CaptureState.VideoRecording -> state.copy(
                captureState = CaptureState.Idle
            )
            is CaptureState.PhotoCapturing -> state
        }

    fun togglePauseVideoRecording(state: CameraUiState): CameraUiState {
        val rec = state.captureState as? CaptureState.VideoRecording ?: return state
        return state.copy(captureState = rec.copy(isPaused = !rec.isPaused))
    }

    fun tickRecordingTimer(state: CameraUiState): CameraUiState {
        val rec = state.captureState as? CaptureState.VideoRecording ?: return state
        if (rec.isPaused) return state
        return state.copy(captureState = rec.copy(elapsedSeconds = rec.elapsedSeconds + 1L))
    }

    fun updateCountdown(state: CameraUiState, secondsRemaining: Int): CameraUiState =
        state.copy(
            captureState = CaptureState.Countdown(secondsRemaining = secondsRemaining),
            activeSheet = ActiveSheet.NONE
        )

    fun beginPhotoCapture(
        state: CameraUiState,
        burstCurrent: Int = 1,
        burstTotal: Int = 1
    ): CameraUiState = state.copy(
        captureState = CaptureState.PhotoCapturing(
            burstCurrent = burstCurrent,
            burstTotal = burstTotal
        ),
        activeSheet = ActiveSheet.NONE
    )

    fun finishPhotoCapture(state: CameraUiState): CameraUiState =
        if (state.captureState is CaptureState.PhotoCapturing || state.captureState is CaptureState.Countdown) {
            state.copy(captureState = CaptureState.Idle)
        } else {
            state
        }

    fun showFocusReticle(state: CameraUiState, normalizedX: Float, normalizedY: Float): CameraUiState =
        state.copy(
            focusReticle = FocusReticleState(
                visible = true,
                normalizedX = normalizedX.coerceIn(0.08f, 0.92f),
                normalizedY = normalizedY.coerceIn(0.12f, 0.88f)
            )
        )

    fun hideFocusReticle(state: CameraUiState): CameraUiState =
        state.copy(focusReticle = state.focusReticle.copy(visible = false))

    fun updateAlgorithmTuning(
        state: CameraUiState,
        transform: (AlgorithmTuningState) -> AlgorithmTuningState
    ): CameraUiState = state.copy(algorithmTuning = transform(state.algorithmTuning))

    fun resetAlgorithmTuning(state: CameraUiState): CameraUiState =
        state.copy(algorithmTuning = AlgorithmTuningState())

    fun updateProParameters(
        state: CameraUiState,
        transform: (ProParametersState) -> ProParametersState
    ): CameraUiState = state.copy(
        proParameters = transform(state.proParameters).copy(hasCustomOverrides = true)
    )

    fun resetProParameters(state: CameraUiState): CameraUiState =
        state.copy(
            proParameters = ProParametersState(
                aspectResolution = AspectResolutionPreset.RATIO_4_3_1080P,
                selectedBracket = BracketFrame.FRAME_2,
                whiteBalance = WhiteBalancePreset.AWB,
                manualFocusProgress = 0.5f,
                shutterSpeed = ShutterSpeedPreset.S_1_250,
                iso = IsoPreset.AUTO,
                hasCustomOverrides = false
            )
        )

    fun updateAutomatedCapture(
        state: CameraUiState,
        transform: (AutomatedCaptureState) -> AutomatedCaptureState
    ): CameraUiState = state.copy(automatedCapture = transform(state.automatedCapture))

    fun resetAutomatedCapture(state: CameraUiState): CameraUiState =
        state.copy(automatedCapture = AutomatedCaptureState(isArmed = false))

    fun armAutomatedCapture(state: CameraUiState): CameraUiState =
        state.copy(
            automatedCapture = state.automatedCapture.copy(isArmed = true),
            activeSheet = ActiveSheet.NONE
        )

    fun updateSettings(
        state: CameraUiState,
        transform: (AppSettingsState) -> AppSettingsState
    ): CameraUiState {
        val nextSettings = transform(state.appSettings)
        return state.copy(
            appSettings = nextSettings,
            trackingOverlay = state.trackingOverlay.copy(visible = nextSettings.showDemoTrackingOverlay),
            performanceWarning = if (nextSettings.simulatePerformanceWarning) {
                "Low FPS Warning: 17 fps (58 ms)"
            } else if (state.lastProcessingTimeMs <= 33) {
                null
            } else {
                state.performanceWarning
            },
            telemetryFps = if (nextSettings.simulatePerformanceWarning) 17.0f else 30.0f,
            telemetryLatencyMs = if (nextSettings.simulatePerformanceWarning) 58.4f else 16.4f,
            isPerformanceWarningDismissed = if (nextSettings.simulatePerformanceWarning) false else state.isPerformanceWarningDismissed
        )
    }

    fun dismissPerformanceWarning(state: CameraUiState): CameraUiState =
        state.copy(
            isPerformanceWarningDismissed = true,
            appSettings = state.appSettings.copy(simulatePerformanceWarning = false)
        )

    fun openOptimizeFromWarning(state: CameraUiState): CameraUiState =
        state.copy(activeSheet = ActiveSheet.ALGORITHM_TUNING)

    fun openSnapshotInspector(state: CameraUiState): CameraUiState =
        state.copy(
            currentRoute = ScreenRoute.SNAPSHOT_INSPECTOR,
            activeSheet = ActiveSheet.NONE
        )

    fun closeSnapshotInspector(state: CameraUiState): CameraUiState =
        state.copy(currentRoute = ScreenRoute.VIEWFINDER)
}
