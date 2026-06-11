package com.rtkcamera.camera

import android.content.Context
import android.util.Log
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Manages CameraX lifecycle and provider initialization.
 */
@Singleton
class CameraManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /**
     * Retrieves the ProcessCameraProvider instance asynchronously.
     */
    suspend fun getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(context).also { future ->
            future.addListener({
                try {
                    continuation.resume(future.get())
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to get camera provider", e)
                    // In a real app, we would resume with an error or throw
                }
            }, ContextCompat.getMainExecutor(context))
        }
    }

    companion object {
        private const val TAG = "CameraManager"
    }
}
