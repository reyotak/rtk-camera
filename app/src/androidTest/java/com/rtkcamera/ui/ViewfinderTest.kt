package com.rtkcamera.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.rtkcamera.ui.theme.RtkCameraTheme
import org.junit.Rule
import org.junit.Test

/**
 * Instrumentation tests for the Penpot RTK-Camera HUD UI and UI state transitions.
 */
class ViewfinderTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun performanceWarning_isDisplayed_whenSetInState() {
        composeTestRule.setContent {
            RtkCameraTheme {
                ViewfinderContent(
                    uiState = CameraUiState(
                        captureState = CaptureState.VideoRecording(elapsedSeconds = 108L),
                        telemetryFps = 17f,
                        telemetryLatencyMs = 58.4f,
                        performanceWarning = "Low FPS Warning: 17 fps (58 ms)"
                    ),
                    callbacks = ViewfinderCallbacks()
                )
            }
        }

        composeTestRule
            .onNodeWithText("Budget: 33ms Exceeded", substring = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Optimize Parameters").assertIsDisplayed()
        composeTestRule.onNodeWithText("Dismiss").assertIsDisplayed()
    }

    @Test
    fun uiStateTransitions_openSheetsAndSnapshotInspector() {
        composeTestRule.setContent {
            RtkCameraTheme {
                var uiState by remember { mutableStateOf(CameraUiState()) }
                ViewfinderContent(
                    uiState = uiState,
                    callbacks = ViewfinderCallbacks(
                        onCycleFlashMode = { uiState = CameraUiTransitions.cycleFlashMode(uiState) },
                        onHelpClick = { uiState = CameraUiTransitions.openSheet(uiState, ActiveSheet.HELP) },
                        onUpdateSettings = { transform -> uiState = CameraUiTransitions.updateSettings(uiState, transform) },
                        onSelectCaptureMode = { mode -> uiState = CameraUiTransitions.selectCaptureMode(uiState, mode) },
                        onOpenSheet = { sheet -> uiState = CameraUiTransitions.openSheet(uiState, sheet) },
                        onDismissSheet = { uiState = CameraUiTransitions.dismissSheet(uiState) },
                        onOpenSnapshotInspector = { uiState = CameraUiTransitions.openSnapshotInspector(uiState) },
                        onCloseSnapshotInspector = { uiState = CameraUiTransitions.closeSnapshotInspector(uiState) }
                    )
                )
            }
        }

        // Open Help Sheet, navigate to Tutorial & About, then close
        composeTestRule.onNodeWithContentDescription("Help").performClick()
        composeTestRule.onNodeWithText("Help & Field Guide").assertIsDisplayed()
        composeTestRule.onNodeWithText("Quick Start Tutorial").performClick()
        composeTestRule.onNodeWithText("Frame & Tap to Focus").assertIsDisplayed()
        composeTestRule.onNodeWithText("Back to Help Menu").performClick()
        composeTestRule.onNodeWithText("About RTK-Camera").performClick()
        composeTestRule.onNodeWithText("v1.0.0").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Close").performClick()

        // Open Algorithm Tuning Drawer (Hi-Fi 02)
        composeTestRule.onNodeWithText("Canny-Edge", substring = true).performClick()
        composeTestRule.onNodeWithText("Algorithm Sandbox & Tuning").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Close").performClick()

        // Open Pro Camera Parameters Sheet (Hi-Fi 05)
        composeTestRule.onNodeWithContentDescription("Pro Camera Parameters").performClick()
        composeTestRule.onNodeWithText("Pro Camera Parameters").assertIsDisplayed()
        composeTestRule.onNodeWithText("ASPECT RATIO & RESOLUTION").assertIsDisplayed()
        composeTestRule.onNodeWithText("4:3 • 1080p").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Close").performClick()

        // Open Automated Capture & Intervalometer Sheet (Hi-Fi 06)
        composeTestRule.onNodeWithContentDescription("Automated Capture & Intervalometer").performClick()
        composeTestRule.onNodeWithText("Automated Capture & Intervalometer").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Close").performClick()

        // Open Snapshot Inspector (Hi-Fi 04) and navigate back
        composeTestRule.onNodeWithContentDescription("Open Snapshot Inspector").performClick()
        composeTestRule.onNodeWithText("Snapshot Inspector").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Back to Viewfinder").performClick()
        composeTestRule.onNodeWithContentDescription("Pro Camera Parameters").assertIsDisplayed()
    }
}
