package com.rtkcamera.camera

import androidx.camera.core.ImageCapture
import java.util.concurrent.Executor

interface CapturePipeline {
    fun takeSnapshot(imageCapture: ImageCapture, executor: Executor)
}
