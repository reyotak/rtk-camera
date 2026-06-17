package com.rtkcamera.camera

import androidx.camera.core.ImageCapture
import com.rtkcamera.nativebridge.AlgorithmProcessor
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import java.util.concurrent.Executor

class CapturePipelineTest {

    @Test
    fun `takeSnapshot calls takePicture on imageCapture`() {
        val galleryRepository = mockk<GalleryRepository>(relaxed = true)
        val algorithmProcessor = mockk<AlgorithmProcessor>(relaxed = true)
        val pipeline = CapturePipelineImpl(galleryRepository, algorithmProcessor)

        val imageCapture = mockk<ImageCapture>(relaxed = true)
        val executor = mockk<Executor>()

        pipeline.takeSnapshot(imageCapture, executor)

        verify { imageCapture.takePicture(executor, any()) }
    }
}
