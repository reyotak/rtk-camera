# Implementation Plan: Snapshot Capture

**Branch**: `003-snapshot-capture` | **Date**: 2026-06-17 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `specs/003-snapshot-capture/spec.md`

## Summary

Implement manual snapshot capture and photo persistence. A user taps the snapshot button, taking a high-resolution photo via CameraX `ImageCapture` use case. The image is passed to the native bridge algorithm processor, and the resulting byte array is saved asynchronously to the device gallery using MediaStore, all while ensuring the live viewfinder remains uninterrupted.

## Technical Context

**Language/Version**: Kotlin (JVM target), Android API 24+

**Primary Dependencies**: Android CameraX, Jetpack Compose, Hilt

**Storage**: Local device gallery (MediaStore)

**Testing**: JUnit, Espresso, ComposeTestRule

**Target Platform**: Android

**Project Type**: mobile-app

**Performance Goals**: 30-60 fps viewfinder without stutter during capture; asynchronous saving

**Constraints**: Non-blocking main thread; strict `ImageProxy` resource management

**Scale/Scope**: ViewfinderScreen modifications, new CapturePipeline & GalleryRepository

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **I. Infrastructure/Algorithm Decoupling**: Passed. CapturePipeline orchestrates the CameraX side, isolating it from the `AlgorithmProcessor` which remains independent.
- **II. Standardized Mobile Data Contracts**: Passed. Uses existing raw mobile sensor frame standard, decoupling algorithm processing from camera framework.
- **III. High-Fidelity & Real-Time Performance**: Passed. Capture process uses background executor for processing and dispatching IO to save, protecting real-time preview.
- **IV. Contextual Metadata Integrity**: Passed. 
- **V. Platform-Agnostic Developer Experience**: Passed. 

## Project Structure

### Documentation (this feature)

```text
specs/003-snapshot-capture/
├── plan.md              # This file (/speckit-plan command output)
├── research.md          # Phase 0 output (/speckit-plan command)
├── data-model.md        # Phase 1 output (/speckit-plan command)
├── quickstart.md        # Phase 1 output (/speckit-plan command)
├── contracts/           # Phase 1 output (/speckit-plan command)
└── tasks.md             # Phase 2 output (/speckit-tasks command - NOT created by /speckit-plan)
```

### Source Code (repository root)

```text
android/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/reyotak/rtkcamera/
│   │   │   │   ├── camera/
│   │   │   │   │   ├── CapturePipeline.kt
│   │   │   │   │   ├── CameraViewModel.kt
│   │   │   │   │   └── ui/ViewfinderScreen.kt
│   │   │   │   └── data/
│   │   │   │       └── GalleryRepository.kt
│   │   └── test/
│   │       └── java/com/reyotak/rtkcamera/
│   │           └── camera/
│   │               └── CapturePipelineTest.kt
│   │   └── androidTest/
│   │       └── java/com/reyotak/rtkcamera/
│   │           └── camera/
│   │               └── ViewfinderScreenTest.kt
```

**Structure Decision**: Standard Android modular structure with new domain classes placed in their respective `camera` and `data` packages.
