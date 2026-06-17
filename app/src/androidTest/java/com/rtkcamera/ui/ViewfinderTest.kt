package com.rtkcamera.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.rtkcamera.camera.AnalysisPipeline
import com.rtkcamera.camera.CameraManager
import com.rtkcamera.nativebridge.NativeLoader
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

/**
 * Instrumentation test for the ViewfinderScreen.
 * Verifies that the UI correctly reacts to performance changes.
 */
class ViewfinderTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val nativeLoader = mockk<NativeLoader>(relaxed = true)
    private val analysisPipeline = mockk<AnalysisPipeline>(relaxed = true)
    private val cameraManager = mockk<CameraManager>(relaxed = true)
    private val capturePipeline = mockk<com.rtkcamera.camera.CapturePipeline>(relaxed = true)

    @Test
    fun performanceWarning_isDisplayed_whenSetInState() {
        val viewModel = CameraViewModel(nativeLoader, analysisPipeline, cameraManager)
        
        // Mock slow processing (100ms > 33ms)
        every { analysisPipeline.getLastProcessingTimeMs() } returns 100

        composeTestRule.setContent {
            ViewfinderScreen(viewModel = viewModel, capturePipeline = capturePipeline)
        }

        // Wait for the background loop in ViewModel to update state (triggered by init)
        composeTestRule.waitUntil(5000) {
            composeTestRule
                .onAllNodesWithText("Low FPS", substring = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule.onNodeWithText("Low FPS", substring = true).assertIsDisplayed()
    }

    @Test
    fun snapshotButton_triggersViewModel() {
        val viewModel = mockk<CameraViewModel>(relaxed = true)

        composeTestRule.setContent {
            ViewfinderScreen(viewModel = viewModel, capturePipeline = capturePipeline)
        }

        composeTestRule.onNodeWithContentDescription("Capture Photo").performClick()

        verify { viewModel.onCaptureTriggered() }
    }
}
