# Interface Contract: CameraManager

The `CameraManager` serves as the primary hardware abstraction layer for accessing camera capabilities.

## `getCameraProvider`

**Description:**
Retrieves the CameraX `ProcessCameraProvider` asynchronously. This provider is used to bind use cases (like `Preview`) to the application lifecycle.

**Signature (Kotlin):**
```kotlin
suspend fun getCameraProvider(context: Context): ProcessCameraProvider
```

**Parameters:**
- `context`: The application or activity context needed to acquire the provider.

**Returns:**
- Returns the initialized `ProcessCameraProvider` instance.

**Error Handling:**
- May throw an `ExecutionException` or `InterruptedException` if the provider fails to initialize (e.g., hardware failure or unsupported device). The UI layer must catch these and display a fallback UI.
