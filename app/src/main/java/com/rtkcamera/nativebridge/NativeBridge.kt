package com.rtkcamera.nativebridge

import android.util.Log
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/**
 * JNI wrapper for native algorithm execution.
 * Implements AlgorithmProcessor to satisfy Principle I (Decoupling).
 */
@Singleton
class NativeBridge @Inject constructor() : AlgorithmProcessor {

    init {
        try {
            System.loadLibrary("rtkcamera")
        } catch (e: UnsatisfiedLinkError) {
            Log.e(TAG, "Failed to load native library: rtkcamera", e)
        }
    }

    override fun process(frame: SensorFrame): Boolean {
        if (!frame.buffer.isDirect) {
            Log.e(TAG, "Buffer must be direct for zero-copy JNI access (Research Decision)")
            return false
        }

        // Standardized metadata contract via JSON (Principle II & IV)
        val metadataJson = try {
            MetadataSerializer.serialize(frame)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to serialize metadata", e)
            ""
        }
        
        return nativeProcessFrame(frame.buffer, metadataJson)
    }

    /**
     * Loads a native plugin dynamically.
     */
    fun loadPlugin(path: String): Boolean {
        return nativeLoadPlugin(path)
    }

    private external fun nativeLoadPlugin(path: String): Boolean

    /**
     * Native call to process the frame.
     * @param buffer The DirectByteBuffer containing raw pixel data.
     * @param metadata JSON string containing spatial and sensor context.
     */
    private external fun nativeProcessFrame(buffer: java.nio.ByteBuffer, metadata: String): Boolean

    companion object {
        private const val TAG = "NativeBridge"
    }
}
