# Research & Decisions

## Camera Engine

- **Decision**: Android CameraX (`camera-core`, `camera-camera2`, `camera-lifecycle`, `camera-view`).
- **Rationale**: CameraX provides a robust, high-level API for camera integration, effectively handling device compatibility issues and simplifying lifecycle management compared to native Camera2. It is the recommended standard for modern Android camera applications.
- **Alternatives considered**: Android Camera2 (too low-level and verbose, requiring extensive boilerplate to handle edge cases across device manufacturers).

## UI Layer Interoperability

- **Decision**: Jetpack Compose `AndroidView` interop wrapper.
- **Rationale**: The project utilizes Jetpack Compose for its UI layout. However, the most optimized rendering surface for CameraX, `PreviewView`, is a traditional Android View. The `AndroidView` interoperability API allows embedding legacy or platform views seamlessly into a Compose hierarchy.
- **Alternatives considered**: Canvas drawing (insufficient performance and complex hookup), building a native Compose camera viewfinder (not yet fully stable/standardized without interop).

## Surface Rendering

- **Decision**: `androidx.camera.view.PreviewView`.
- **Rationale**: It is optimized specifically for CameraX preview use cases, automatically handling surface scaling, aspect ratios, and orientation changes without manual math.
- **Alternatives considered**: `TextureView` or `SurfaceView` (requires manual calculation for scaling and orientation handling, which is error-prone).

## State Management and Lifecycle Binding

- **Decision**: Bind the CameraX `Preview` usecase using the current Compose `LocalLifecycleOwner.current` inside a `DisposableEffect`. Keep UI overlay state in a view model or separate Compose state.
- **Rationale**: Ensures the camera correctly releases resources when the Compose screen leaves the composition or the app backgrounds. Separating the UI state from the camera thread ensures that overlay updates don't block the camera frames.
- **Alternatives considered**: Binding the camera in the Activity `onCreate` (breaks Compose component isolation and reusability).
