# Research & Architectural Decisions

This document captures the architectural choices derived from the feature specification and technical constraints.

## Target Platform & Language
- **Decision**: Kotlin (JVM target), Android API 24+.
- **Rationale**: Standard development stack for modern Android apps, matching the existing RTK Camera application infrastructure.

## Camera & UI Integration
- **Decision**: Android CameraX with Jetpack Compose integration.
- **Rationale**: CameraX simplifies camera lifecycle management and device compatibility. `ImageCapture` use case will be bound alongside the `Preview` use case in `ViewfinderScreen.kt`. The snapshot button will trigger `CameraViewModel.onCaptureTriggered()`, using a `StateFlow` or `SharedFlow` to signal `ViewfinderScreen.kt` to invoke `CapturePipeline.takeSnapshot`.

## Image Processing & Saving Pipeline
- **Decision**: `CapturePipeline` handles execution, native bridge processing, and persistence via `GalleryRepository`.
- **Rationale**: 
  - Uses `ContextCompat.getMainExecutor(context)` for `ImageCapture.takePicture`.
  - Background thread processing for the `AlgorithmProcessor` (native bridge) prevents UI blockage.
  - Converts Direct `ByteBuffer` to `ByteArray` and writes asynchronously to local storage.
  - Explicit resource management with `finally` block to close `ImageProxy` prevents memory leaks.

## Testing Strategy
- **Decision**: JUnit for unit tests, Espresso/ComposeTestRule for UI/Integration.
- **Rationale**: Standard Android testing tools. Ensures `CameraViewModel` and `CapturePipeline` are isolated via mocks, and the UI integration works end-to-end.
