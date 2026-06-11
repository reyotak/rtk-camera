package com.rtkcamera.nativebridge

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Spatial and sensor context associated with a frame.
 * Standardized via JSON contract (Principle II).
 */
@Serializable
data class CameraMetadata(
    val width: Int,
    val height: Int,
    val stride: Int,
    val format: String, // e.g., "YUV_420_888", "RGBA_8888"
    val orientation: Int, // Degrees: 0, 90, 180, 270
    val timestamp: Long // Epoch nanos
)

/**
 * Service to serialize SensorFrame metadata to JSON.
 */
object MetadataSerializer {
    
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    /**
     * Serializes frame metadata to a JSON string.
     */
    fun serialize(frame: SensorFrame): String {
        val metadata = CameraMetadata(
            width = frame.width,
            height = frame.height,
            stride = frame.stride,
            format = frame.format,
            orientation = frame.orientation,
            timestamp = frame.timestamp
        )
        return json.encodeToString(metadata)
    }
}
