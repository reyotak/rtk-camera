package com.rtkcamera.camera

import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import com.rtkcamera.nativebridge.AlgorithmProcessor
import com.rtkcamera.nativebridge.SensorFrame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import javax.inject.Inject

class CapturePipelineImpl @Inject constructor(
    private val galleryRepository: GalleryRepository,
    private val algorithmProcessor: AlgorithmProcessor
) : CapturePipeline {

    override fun takeSnapshot(imageCapture: ImageCapture, executor: Executor) {
        imageCapture.takePicture(
            executor,
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    CoroutineScope(Dispatchers.IO).launch {
                        image.use { image ->
                            val plane = image.planes[0]
                            val buffer = plane.buffer

                            // Pass frame to algorithm processor if buffer is direct
                            if (buffer.isDirect) {
                                val frame = SensorFrame(
                                    buffer = buffer,
                                    width = image.width,
                                    height = image.height,
                                    stride = plane.rowStride,
                                    format = "JPEG",
                                    orientation = image.imageInfo.rotationDegrees,
                                    timestamp = image.imageInfo.timestamp
                                )
                                algorithmProcessor.process(frame)
                            }

                            // Read bytes from the buffer and save
                            buffer.rewind()
                            val bytes = ByteArray(buffer.remaining())
                            buffer.get(bytes)

                            val fileName = "RTK_IMG_${System.currentTimeMillis()}.jpg"
                            galleryRepository.saveImage(bytes, fileName)
                        }
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                }
            }
        )
    }
}
