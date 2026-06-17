# Interface Contract: CapturePipeline

The `CapturePipeline` manages the orchestration of snapping a photo via CameraX, passing it to algorithms, and handling the resource lifecycle of the `ImageProxy`.

## Kotlin Interface

```kotlin
interface CapturePipeline {
    /**
     * Initiates the capture process.
     * 
     * @param imageCapture The configured CameraX ImageCapture use case.
     * @param executor The executor to run the capture callback on (typically main executor).
     */
    fun takeSnapshot(imageCapture: ImageCapture, executor: Executor)
}
```

## Behavior Contract
1.  **Execution**: Must invoke `imageCapture.takePicture()` with the provided `executor`.
2.  **Processing**: On success (`OnImageCapturedCallback.onCaptureSuccess`), must extract the frame data from the `ImageProxy` and pass it to the native bridge (`AlgorithmProcessor`) on a background thread.
3.  **Persistence**: After processing, must delegate the byte data to `GalleryRepository` for asynchronous saving.
4.  **Resource Management**: Must guarantee `imageProxy.close()` is called in a `finally` block or equivalent construct to avoid memory leaks.
