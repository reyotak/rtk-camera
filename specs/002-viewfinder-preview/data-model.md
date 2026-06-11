# Data Model

Because this feature is primarily focused on hardware integration (Camera API) and UI rendering (Jetpack Compose Viewfinder), there are no persistently stored entities or complex backend models associated with this feature.

The state is managed in-memory and consists of the following transient entities:

## Transient Entities

### 1. Camera Provider State
- **Description**: The asynchronous state representing the availability of the `ProcessCameraProvider`.
- **Attributes**:
  - `isInitialized` (Boolean): True when the camera hardware is successfully accessed.
  - `provider` (ProcessCameraProvider): The instance used to bind use cases.

### 2. Viewfinder UI State
- **Description**: The state driving the UI overlays on top of the camera.
- **Attributes**:
  - `hasCameraPermission` (Boolean): Indicates whether the user has granted the required `CAMERA` permission.
  - `orientation` (Enum/Int): The current device orientation to adapt UI elements if necessary (PreviewView handles the stream orientation internally).

### 3. Camera Use Cases
- **Description**: The active use cases bound to the camera lifecycle.
- **Components**:
  - `Preview`: The CameraX use case responsible for delivering frames to the `PreviewView`.
