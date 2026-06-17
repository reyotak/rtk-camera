package com.rtkcamera.ui

import com.rtkcamera.camera.AnalysisPipeline
import com.rtkcamera.camera.CameraManager
import com.rtkcamera.nativebridge.NativeLoader
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CameraViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: CameraViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        val nativeLoader = mockk<NativeLoader>()
        val analysisPipeline = mockk<AnalysisPipeline>(relaxed = true)
        val cameraManager = mockk<CameraManager>()
        viewModel = CameraViewModel(nativeLoader, analysisPipeline, cameraManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onCaptureTriggered emits event to captureFlow`() = runTest {
        val events = mutableListOf<Unit>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.captureEventFlow.collect {
                events.add(Unit)
            }
        }
        viewModel.onCaptureTriggered()
        assert(events.isNotEmpty()) { "Expected at least one capture event to be emitted" }
    }
}
