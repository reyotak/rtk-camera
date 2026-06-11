package com.rtkcamera.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.rtkcamera.camera.AnalysisPipeline
import com.rtkcamera.nativebridge.NativeLoader
import io.mockk.every
import io.mockk.mockk
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

    @Test
    fun performanceWarning_isDisplayed_whenSetInState() {
        val viewModel = CameraViewModel(nativeLoader, analysisPipeline)
        
        // Mock slow processing (100ms > 33ms)
        every { analysisPipeline.getLastProcessingTimeMs() } returns 100

        composeTestRule.setContent {
            ViewfinderScreen(viewModel = viewModel)
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
}
