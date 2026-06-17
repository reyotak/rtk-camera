# Data Model: Snapshot Capture

## Entities

### `CapturedImage`
Represents the raw/initial frame data captured from the camera sensor before any extensive processing.

- **Attributes**:
  - `imageProxy`: `ImageProxy` (Android CameraX specific interface, closed after use)
  - `format`: `Int` (Image format, e.g., JPEG, YUV)
  - `width`: `Int`
  - `height`: `Int`
  - `rotationDegrees`: `Int`

### `ProcessedImage`
Represents the final image data ready to be saved to the device gallery.

- **Attributes**:
  - `data`: `ByteArray` (Extracted from ByteBuffer, potentially after algorithm enhancement)
  - `mimeType`: `String` (e.g., "image/jpeg")
  - `timestamp`: `Long` (Capture time)

### `GallerySaveResult`
Represents the outcome of persisting the image to the device gallery.

- **Attributes**:
  - `success`: `Boolean`
  - `uri`: `Uri?` (The MediaStore URI of the saved image if successful)
  - `errorMessage`: `String?` (If save failed)
