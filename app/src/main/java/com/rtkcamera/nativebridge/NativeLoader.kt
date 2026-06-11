package com.rtkcamera.nativebridge

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service to load dynamic native libraries (.so files).
 * Satisfies User Story 1: Plug-and-Play Algorithm Integration.
 */
@Singleton
class NativeLoader @Inject constructor(
    private val nativeBridge: NativeBridge
) {

    /**
     * Loads a native library from an absolute path via the NativeBridge.
     * @param algorithm The algorithm info containing the library path.
     * @return true if loaded successfully, false otherwise.
     */
    fun load(algorithm: AlgorithmInfo): Boolean {
        return try {
            val success = nativeBridge.loadPlugin(algorithm.libraryPath)
            if (success) {
                Log.d(TAG, "Successfully loaded algorithm: ${algorithm.name}")
            }
            success
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load algorithm: ${algorithm.name}", e)
            false
        }
    }

    companion object {
        private const val TAG = "NativeLoader"
    }
}
