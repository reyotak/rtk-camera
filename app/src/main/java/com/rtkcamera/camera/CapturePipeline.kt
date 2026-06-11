package com.rtkcamera.camera

import android.content.Context
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.core.content.ContextCompat
import com.rtkcamera.nativebridge.AlgorithmProcessor
import com.rtkcamera.nativebridge.SensorFrame
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages full-resolution image capture and algorithm processing.
 * Satisfies User Story 3: High-Fidelity Capture & Persistence.
 */
@Singleton
class CapturePipeline @Inject constructor(
    private val algorithmProcessor: AlgorithmProcessor,
    private val galleryRepository: GalleryRepository
) {

    fun takeSnapshot(imageCapture: ImageCapture, context: Context) {
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    processAndSave(image)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "Capture failed", exception)
                }
            }
        )
    }

    private fun processAndSave(image: ImageProxy) {
        val plane = image.planes[0]
        val buffer = plane.buffer

        // Audit resolution (SC-004)
        Log.d(TAG, "Captured resolution: ${image.width}x${image.height}")

        if (buffer.isDirect) {
            val frame = SensorFrame(
                buffer = buffer,
                width = image.width,
                height = image.height,
                stride = plane.rowStride,
                format = "RGBA_8888", // Snapshot format might differ, but assuming RGBA for prototype
                orientation = image.imageInfo.rotationDegrees,
                timestamp = image.imageInfo.timestamp
            )

            // Process via native algorithm (Principle III: high-res path is distinct)
            algorithmProcessor.process(frame)

            // Save to gallery
            val data = ByteArray(buffer.remaining())
            buffer.get(data)
            galleryRepository.saveImage(data, "RTK_${System.currentTimeMillis()}.jpg")
        }

        image.close()
    }

    companion object {
        private const val TAG = "CapturePipeline"
    }
}
