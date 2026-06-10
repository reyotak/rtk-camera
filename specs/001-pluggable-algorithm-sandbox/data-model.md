# Data Model: Pluggable Camera Algorithm Sandbox

## Core Entities

### 1. SensorFrame (Native/JVM Interface)
Represents a single frame of camera data passed to the algorithm.
- **Buffer**: `java.nio.ByteBuffer` (Direct) / `uint8_t*`
- **Capacity**: Total bytes in buffer.
- **Metadata**: JSON string (see Contracts).

### 2. CameraMetadata (JSON Schema)
The spatial and sensor context associated with a frame.
- **width**: `Int` (Pixel width)
- **height**: `Int` (Pixel height)
- **stride**: `Int` (Bytes per row)
- **format**: `String` (e.g., "YUV_420_888", "RGBA_8888")
- **orientation**: `Int` (Degrees: 0, 90, 180, 270)
- **timestamp**: `Long` (Epoch nanos)

### 3. AlgorithmInfo
Registry information for available plugins.
- **id**: `String` (Unique identifier)
- **name**: `String` (Display name in UI)
- **libraryPath**: `String` (Path to the .so file)

### 4. ProcessingState (UI State)
Current status of the execution pipeline.
- **activeAlgorithmId**: `String?`
- **isProcessing**: `Boolean`
- **lastProcessingTimeMs**: `Long`
- **performanceWarning**: `String?` (Displayed if > 33ms)

## State Transitions
1. **Idle**: No algorithm selected or camera inactive.
2. **Loading**: Dynamic library being loaded via `System.load()`.
3. **Streaming**: Real-time analysis path active.
4. **Capturing**: High-resolution snapshot path active (Streaming paused).
5. **Error**: Plugin load failure or crash.
