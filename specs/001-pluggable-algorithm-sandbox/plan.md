# Implementation Plan: Pluggable Camera Algorithm Sandbox

**Branch**: `[001-pluggable-algorithm-sandbox]` | **Date**: 2026-06-10 | **Spec**: [specs/001-pluggable-algorithm-sandbox/spec.md](spec.md)

**Input**: Feature specification from `specs/001-pluggable-algorithm-sandbox/spec.md`

## Summary

The goal is to implement a modular "Algorithm Sandbox" for RTK Camera using a native plugin architecture. We will use Jetpack Compose for the UI, Hilt for dependency injection, and CameraX for sensor management. The core processing will happen in a C++/Rust native layer via JNI with `DirectByteBuffer` for zero-copy performance. A rigorous testing framework will cover both Kotlin (JUnit/MockK/Compose) and Native (GoogleTest) layers.

## Technical Context

**Language/Version**: Kotlin 1.9+, C++ 17 / Rust 1.70+

**Primary Dependencies**: Jetpack Compose (Material 3), Hilt, CameraX, Android NDK, JNI, Kotlin Coroutines/Flow

**Storage**: MediaStore (public gallery for snapshots)

**Testing**: 
- **Kotlin**: JUnit 5, MockK, Compose Test Rule
- **Native**: GoogleTest (GTest)

**Target Platform**: Android (API 24+)

**Project Type**: mobile-app with native-lib

**Performance Goals**: < 33ms processing per frame for 30fps real-time preview; zero-copy buffer passing.

**Constraints**: 
- Strict separation between infrastructure and algorithm layers (Constitution Principle I).
- Stable metadata contracts via JSON (Constitution Principle II).
- Frame-dropping logic for UI stability (Constitution Principle III).

**Scale/Scope**: Single feature module for now, extensible to support multiple dynamic .so plugins.

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

1. **Principle I (Decoupling)**: 🟢 PASS. Use of Hilt to inject `AlgorithmProcessor` abstractions ensures the UI and Camera infrastructure don't depend on specific algorithm implementations.
2. **Principle II (Contracts)**: 🟢 PASS. Standardized C header and JSON metadata bundle ensure stable interfaces.
3. **Principle III (Performance)**: 🟢 PASS. Real-time path (ImageAnalysis) and Snapshot path (ImageCapture) are distinct.
4. **Principle IV (Metadata)**: 🟢 PASS. JNI signature explicitly includes a JSON metadata string.
5. **Principle V (Developer Experience)**: 🟢 PASS. `Algorithm Entry Point` is a single C header, abstracting mobile lifecycle.

## Project Structure

### Documentation (this feature)

```text
specs/001-pluggable-algorithm-sandbox/
├── plan.md              # This file
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
├── contracts/           # Phase 1 output (JNI Header, JSON Schema)
└── tasks.md             # Phase 2 output
```

### Source Code (repository root)

```text
app/src/main/
├── java/com/rtkcamera/
│   ├── camera/          # CameraX implementation
│   ├── di/              # Hilt Modules
│   ├── native/          # JNI Bridge and Algorithm Loader
│   └── ui/              # Compose screens and ViewModels
├── cpp/
│   ├── bridge/          # JNI glue code
│   ├── core/            # Common native types
│   └── plugin/          # Implementation of the C contract
├── test/                # Kotlin Unit Tests
├── androidTest/         # Compose UI Tests
└── cpptest/             # GoogleTest suites
```

**Structure Decision**: Standard Android multi-layered project with a dedicated `cpp` directory for native logic and `cpptest` for native validation.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| N/A | | |
