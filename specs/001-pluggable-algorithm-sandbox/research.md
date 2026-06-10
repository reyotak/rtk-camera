# Research: Pluggable Camera Algorithm Sandbox

## Decision: JNI with DirectByteBuffer
- **Rationale**: To achieve real-time 30fps processing, we must avoid copying large image buffers between the JVM and Native layers. `DirectByteBuffer` allows the native layer to access the same memory address as the JVM, significantly reducing latency.
- **Alternatives Considered**: 
  - `GetByteArrayElements`: Involves copies or pinning, which can lead to GC stalls or increased memory pressure.
  - `HardwareBuffer`: Excellent for GPU interop, but more complex for CPU-based algorithm processing in initial prototype.

## Decision: Hilt for Algorithm Loading
- **Rationale**: By using Hilt, we can provide a `MockAlgorithmProcessor` during Kotlin unit tests without ever loading a `.so` file, ensuring fast and reliable JVM tests.
- **Alternatives Considered**: 
  - Manual Singleton: Harder to mock and leads to tight coupling with the Android Lifecycle.

## Decision: JSON for Metadata Contract
- **Rationale**: Metadata for sensors (GPS, IMU, Orientation) is heterogeneous. JSON provides an extensible format that both C++ (via `nlohmann/json`) and Kotlin (via `kotlinx.serialization`) can handle easily.
- **Alternatives Considered**: 
  - Protobuf: Higher performance but adds build complexity for a research-oriented sandbox.
  - FlatBuffers: Low overhead but JSON is more accessible for rapid prototyping by non-mobile specialists.

## Best Practice: GoogleTest (GTest) for Native Logic
- **Rationale**: Running algorithm tests directly on the development machine (via CMake) is significantly faster than deploying to an Android device for every iteration.
- **Implementation**: Native code will be structured to separate the JNI "glue" from the core algorithm logic, allowing the core to be compiled and tested in a standard desktop environment.
