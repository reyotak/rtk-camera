package com.rtkcamera.nativebridge

import java.nio.ByteBuffer

/**
 * Represents a single frame of camera data passed to the algorithm.
 */
data class SensorFrame(
    val buffer: ByteBuffer, // Direct ByteBuffer for zero-copy
    val width: Int,
    val height: Int,
    val stride: Int,
    val format: String,
    val orientation: Int,
    val timestamp: Long
)

/**
 * Interface for camera algorithm processing.
 * Decouples the UI/Camera infra from specific algorithm implementations (Principle I).
 */
interface AlgorithmProcessor {
    fun process(frame: SensorFrame): Boolean
}
