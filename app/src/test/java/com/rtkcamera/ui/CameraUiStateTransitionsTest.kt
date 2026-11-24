package com.rtkcamera.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests verifying all Penpot UI state transitions (Hi-Fi 01a–07).
 */
class CameraUiStateTransitionsTest {

    @Test
    fun defaultState_startsInPhotoMode() {
        val initial = CameraUiState()
        assertEquals(CaptureMode.PHOTO, initial.captureMode)
        assertEquals(CaptureState.Idle, initial.captureState)
        assertEquals(FlashMode.OFF, initial.flashMode)
    }

    @Test
    fun selectAndToggleCaptureMode_switchesCleanlyInIdle() {
        val initial = CameraUiState(captureMode = CaptureMode.VIDEO)
        val photo = CameraUiTransitions.selectCaptureMode(initial, CaptureMode.PHOTO)
        assertEquals(CaptureMode.PHOTO, photo.captureMode)

        val backToVideo = CameraUiTransitions.toggleCaptureMode(photo)
        assertEquals(CaptureMode.VIDEO, backToVideo.captureMode)
    }

    @Test
    fun flashMode_cyclesOffOnAutoOff() {
        val off = CameraUiState(flashMode = FlashMode.OFF)
        val on = CameraUiTransitions.cycleFlashMode(off)
        assertEquals(FlashMode.ON, on.flashMode)
        val auto = CameraUiTransitions.cycleFlashMode(on)
        assertEquals(FlashMode.AUTO, auto.flashMode)
        val backOff = CameraUiTransitions.cycleFlashMode(auto)
        assertEquals(FlashMode.OFF, backOff.flashMode)
    }

    @Test
    fun toggleCaptureMode_ignoredWhileRecording() {
        val recording = CameraUiState(
            captureMode = CaptureMode.VIDEO,
            captureState = CaptureState.VideoRecording()
        )
        val unchanged = CameraUiTransitions.selectCaptureMode(recording, CaptureMode.PHOTO)
        assertEquals(CaptureMode.VIDEO, unchanged.captureMode)
    }

    @Test
    fun videoRecording_startPauseTickAndStop() {
        val idle = CameraUiState(captureMode = CaptureMode.VIDEO, activeSheet = ActiveSheet.ALGORITHM_TUNING)
        val started = CameraUiTransitions.startOrStopVideoRecording(idle)
        assertEquals(CaptureState.VideoRecording(isPaused = false, elapsedSeconds = 0L), started.captureState)
        assertEquals(ActiveSheet.NONE, started.activeSheet)

        val ticked = CameraUiTransitions.tickRecordingTimer(started)
        assertEquals(1L, (ticked.captureState as CaptureState.VideoRecording).elapsedSeconds)

        val paused = CameraUiTransitions.togglePauseVideoRecording(ticked)
        assertTrue((paused.captureState as CaptureState.VideoRecording).isPaused)

        val pausedTick = CameraUiTransitions.tickRecordingTimer(paused)
        assertEquals(1L, (pausedTick.captureState as CaptureState.VideoRecording).elapsedSeconds)

        val stopped = CameraUiTransitions.startOrStopVideoRecording(pausedTick)
        assertEquals(CaptureState.Idle, stopped.captureState)
    }

    @Test
    fun photoCapture_countdownAndBurstLifecycle() {
        val idle = CameraUiState(captureMode = CaptureMode.PHOTO, activeSheet = ActiveSheet.PRO_PARAMETERS)
        val countdown = CameraUiTransitions.updateCountdown(idle, 3)
        assertEquals(CaptureState.Countdown(3), countdown.captureState)
        assertEquals(ActiveSheet.NONE, countdown.activeSheet)

        val capturing = CameraUiTransitions.beginPhotoCapture(countdown, burstCurrent = 2, burstTotal = 5)
        assertEquals(CaptureState.PhotoCapturing(burstCurrent = 2, burstTotal = 5), capturing.captureState)

        val finished = CameraUiTransitions.finishPhotoCapture(capturing)
        assertEquals(CaptureState.Idle, finished.captureState)
    }

    @Test
    fun focusReticle_clampsAndHides() {
        val shown = CameraUiTransitions.showFocusReticle(CameraUiState(), 0.01f, 0.99f)
        assertTrue(shown.focusReticle.visible)
        assertEquals(0.08f, shown.focusReticle.normalizedX, 0.001f)
        assertEquals(0.88f, shown.focusReticle.normalizedY, 0.001f)

        val hidden = CameraUiTransitions.hideFocusReticle(shown)
        assertFalse(hidden.focusReticle.visible)
    }

    @Test
    fun sheets_openUpdateResetAndDismiss() {
        val base = CameraUiState()

        val algoOpen = CameraUiTransitions.openSheet(base, ActiveSheet.ALGORITHM_TUNING)
        assertEquals(ActiveSheet.ALGORITHM_TUNING, algoOpen.activeSheet)

        val tuned = CameraUiTransitions.updateAlgorithmTuning(algoOpen) {
            it.copy(lowThreshold = 90, kernelSize = GaussianKernelSize.KERNEL_7X7, saveRawWithSnapshot = false)
        }
        assertEquals(90, tuned.algorithmTuning.lowThreshold)
        assertEquals(GaussianKernelSize.KERNEL_7X7, tuned.algorithmTuning.kernelSize)
        assertFalse(tuned.algorithmTuning.saveRawWithSnapshot)

        val resetAlgo = CameraUiTransitions.resetAlgorithmTuning(tuned)
        assertEquals(45, resetAlgo.algorithmTuning.lowThreshold)
        assertEquals(GaussianKernelSize.KERNEL_3X3, resetAlgo.algorithmTuning.kernelSize)
        assertTrue(resetAlgo.algorithmTuning.saveRawWithSnapshot)

        val proOpen = CameraUiTransitions.openSheet(resetAlgo, ActiveSheet.PRO_PARAMETERS)
        val proUpdated = CameraUiTransitions.updateProParameters(proOpen) {
            it.copy(
                aspectResolution = AspectResolutionPreset.RATIO_16_9_4K,
                whiteBalance = WhiteBalancePreset.TUNGSTEN,
                iso = IsoPreset.ISO_400
            )
        }
        assertTrue(proUpdated.proParameters.hasCustomOverrides)
        assertNotNull(proUpdated.proParameters.summaryBadge)

        val proReset = CameraUiTransitions.resetProParameters(proUpdated)
        assertFalse(proReset.proParameters.hasCustomOverrides)
        assertNull(proReset.proParameters.summaryBadge)
        assertEquals(AspectResolutionPreset.RATIO_4_3_1080P, proReset.proParameters.aspectResolution)
        assertEquals(WhiteBalancePreset.AWB, proReset.proParameters.whiteBalance)
        assertEquals(IsoPreset.AUTO, proReset.proParameters.iso)

        val autoOpen = CameraUiTransitions.openSheet(proReset, ActiveSheet.AUTOMATED_CAPTURE)
        val armed = CameraUiTransitions.armAutomatedCapture(autoOpen)
        assertTrue(armed.automatedCapture.isArmed)
        assertNotNull(armed.automatedCapture.armedSummaryBadge)
        assertEquals(ActiveSheet.NONE, armed.activeSheet)
    }

    @Test
    fun settingsSheet_togglesOverlaysAndDemoStates() {
        val base = CameraUiState()
        val updated = CameraUiTransitions.updateSettings(base) {
            it.copy(
                showRuleOfThirdsGrid = true,
                showAspectFramingGuides = true,
                showDemoTrackingOverlay = true,
                simulatePerformanceWarning = true
            )
        }
        assertTrue(updated.appSettings.showRuleOfThirdsGrid)
        assertTrue(updated.appSettings.showAspectFramingGuides)
        assertTrue(updated.trackingOverlay.visible)
        assertNotNull(updated.performanceWarning)
    }

    @Test
    fun performanceWarning_dismissAndOptimize() {
        val warned = CameraUiState(
            performanceWarning = "Low FPS Warning: 17 fps (58 ms)",
            captureState = CaptureState.VideoRecording()
        )
        val optimized = CameraUiTransitions.openOptimizeFromWarning(warned)
        assertEquals(ActiveSheet.ALGORITHM_TUNING, optimized.activeSheet)

        val dismissed = CameraUiTransitions.dismissPerformanceWarning(warned)
        assertTrue(dismissed.isPerformanceWarningDismissed)
    }

    @Test
    fun snapshotInspector_openAndCloseNavigation() {
        val initial = CameraUiState(activeSheet = ActiveSheet.ALGORITHM_TUNING)
        val inspector = CameraUiTransitions.openSnapshotInspector(initial)
        assertEquals(ScreenRoute.SNAPSHOT_INSPECTOR, inspector.currentRoute)
        assertEquals(ActiveSheet.NONE, inspector.activeSheet)

        val back = CameraUiTransitions.closeSnapshotInspector(inspector)
        assertEquals(ScreenRoute.VIEWFINDER, back.currentRoute)
    }

    @Test
    fun formatRecordingTimer_formatsHoursMinutesSeconds() {
        assertEquals("00:00:00", formatRecordingTimer(0L))
        assertEquals("00:04:12", formatRecordingTimer(252L))
        assertEquals("01:02:03", formatRecordingTimer(3723L))
    }

    @Test
    fun automatedCapture_computesDynamicEstimatedSession() {
        val state = AutomatedCaptureState(
            burstCount = BurstCountOption.TEN,
            intervalDelay = IntervalDelayOption.S_0_5
        )
        assertEquals("10 Frames Selected", state.burstSummaryLabel)
        assertTrue(state.estimatedSessionText.contains("10 Burst Frames @ 0.5s interval = ~5s"))
    }
}
