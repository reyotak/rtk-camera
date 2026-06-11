package com.rtkcamera.camera

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.rtkcamera.nativebridge.AlgorithmProcessor
import com.rtkcamera.nativebridge.SensorFrame
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Connects CameraX ImageAnalysis to the AlgorithmProcessor.
 * Satisfies Principle III (Performance) via frame-dropping if busy.
 */
@Singleton
class AnalysisPipeline @Inject constructor(
    private val algorithmProcessor: AlgorithmProcessor
) : ImageAnalysis.Analyzer {

    private var lastProcessingTimeMs: Long = 0

    override fun analyze(image: ImageProxy) {
        val startTime = System.currentTimeMillis()

        // ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST is handled by CameraX
        // but we still want to monitor performance.

        val plane = image.planes[0]
        val buffer = plane.buffer

        if (buffer.isDirect) {
            val frame = SensorFrame(
                buffer = buffer,
                width = image.width,
                height = image.height,
                stride = plane.rowStride,
                format = "YUV_420_888", // CameraX usually provides YUV
                orientation = image.imageInfo.rotationDegrees,
                timestamp = image.imageInfo.timestamp
            )

            try {
                algorithmProcessor.process(frame)
            } catch (e: Exception) {
                Log.e(TAG, "Error during algorithm processing", e)
            }
        } else {
            Log.e(TAG, "ImageProxy buffer is NOT direct. Skipping frame.")
        }

        lastProcessingTimeMs = System.currentTimeMillis() - startTime
        
        // Always close the image proxy to avoid blocking the pipeline
        image.close()
    }

    fun getLastProcessingTimeMs(): Long = lastProcessingTimeMs

    companion object {
        private const val TAG = "AnalysisPipeline"
    }
}
