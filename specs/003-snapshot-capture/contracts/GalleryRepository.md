# Interface Contract: GalleryRepository

The `GalleryRepository` is responsible for asynchronously persisting raw or processed byte arrays to the Android MediaStore so they appear in the device's system gallery.

## Kotlin Interface

```kotlin
interface GalleryRepository {
    /**
     * Saves image bytes to the device's local photo gallery asynchronously.
     * 
     * @param imageBytes The raw JPEG data to save.
     * @return A Result containing the URI of the saved image on success, or an Exception on failure.
     */
    suspend fun saveImage(imageBytes: ByteArray): Result<Uri>
}
```

## Behavior Contract
1.  **Threading**: Must execute file I/O operations on an I/O dispatcher (`Dispatchers.IO`) to ensure the main thread is never blocked.
2.  **MediaStore Interaction**: Must create a new record in the `MediaStore.Images.Media` collection and write the bytes to the provided output stream.
3.  **Result Handling**: Must return a Kotlin `Result` capturing either the successful `Uri` or the underlying `IOException`/`SecurityException`.
