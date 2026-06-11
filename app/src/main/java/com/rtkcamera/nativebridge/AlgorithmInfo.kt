package com.rtkcamera.nativebridge

/**
 * Registry information for available plugins.
 */
data class AlgorithmInfo(
    val id: String, // Unique identifier
    val name: String, // Display name in UI
    val libraryPath: String // Path to the .so file
)
