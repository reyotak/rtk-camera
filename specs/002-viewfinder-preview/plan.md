# Implementation Plan: Live Camera Preview

**Branch**: `feat/5-viewfinder-preview` | **Date**: 2026-06-11 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `/specs/002-viewfinder-preview/spec.md`

## Summary

Implement a real-time live camera feed inside the application using Android CameraX and Jetpack Compose. The `PreviewView` from CameraX will be wrapped in an `AndroidView` within the Compose layout, securely bound to the application lifecycle to provide a real-time viewfinder experience that adapts to device orientation while keeping overlay interactive elements responsive.

## Technical Context

**Language/Version**: Kotlin

**Primary Dependencies**: Android CameraX (`camera-core`, `camera-camera2`, `camera-lifecycle`, `camera-view`), Jetpack Compose.

**Storage**: N/A

**Testing**: JUnit, Espresso (UI tests)

**Target Platform**: Android API 24+

**Project Type**: mobile-app

**Performance Goals**: Smooth real-time preview (e.g., 30/60 fps) without impacting UI interaction latency.

**Constraints**: Proper state separation to prevent UI overlays from blocking or lagging the camera render thread.

**Scale/Scope**: Single viewfinder screen component.

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **I. Infrastructure/Algorithm Decoupling**: Passed. The UI layer (Viewfinder) uses standard Android/CameraX abstractions. It does not mix algorithm logic.
- **III. High-Fidelity & Real-Time Performance**: Passed. Relies on CameraX's optimized `PreviewView` for real-time visualization.
- **V. Platform-Agnostic Developer Experience**: Passed. Setting up the viewfinder cleanly provides a solid baseline for algorithm developers without requiring them to handle lifecycle boilerplate.

## Project Structure

### Documentation (this feature)

```text
specs/002-viewfinder-preview/
├── plan.md              # This file (/speckit-plan command output)
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
├── contracts/           # Phase 1 output
│   └── CameraManager.md
└── tasks.md             # Phase 2 output (to be created)
```

### Source Code (repository root)

```text
app/src/main/
├── java/com/rtk/camera/
│   ├── ui/
│   │   ├── components/
│   │   │   └── ViewfinderScreen.kt
│   │   └── viewmodels/
│   │       └── CameraViewModel.kt
│   └── hardware/
│       └── CameraManager.kt
```

**Structure Decision**: The project uses a standard Android application layout utilizing Compose for UI and a dedicated hardware layer for camera interactions.
