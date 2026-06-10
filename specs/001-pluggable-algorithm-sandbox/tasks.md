# Tasks: Pluggable Camera Algorithm Sandbox

**Input**: Design documents from `/specs/001-pluggable-algorithm-sandbox/`

**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/

**Tests**: Tests are INCLUDED as requested in the Implementation Plan (JUnit 5, MockK, Compose Test Rule, GoogleTest).

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [ ] T001 Create project structure per implementation plan (java, cpp, cpptest folders)
- [ ] T002 Initialize Hilt, CameraX, and Compose dependencies in `app/build.gradle`
- [ ] T003 [P] Configure NDK/CMake for JNI and GoogleTest in `app/src/main/cpp/CMakeLists.txt`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

- [ ] T004 Implement `AlgorithmProcessor` interface in `app/src/main/java/com/rtkcamera/native/AlgorithmProcessor.kt`
- [ ] T005 [P] Create Hilt modules for Camera and Native components in `app/src/main/java/com/rtkcamera/di/NativeModule.kt`
- [ ] T006 [P] Implement `NativeBridge` (JNI wrapper) in `app/src/main/java/com/rtkcamera/native/NativeBridge.kt`
- [ ] T007 [P] Setup GoogleTest structure in `app/src/main/cpptest/CMakeLists.txt`
- [ ] T008 Create `CameraManager` for lifecycle management in `app/src/main/java/com/rtkcamera/camera/CameraManager.kt`

**Checkpoint**: Foundation ready - user story implementation can now begin

---

## Phase 3: User Story 1 - Plug-and-Play Algorithm Integration (Priority: P1) 🎯 MVP

**Goal**: Load and execute custom native logic without full app rebuilds.

**Independent Test**: Use a dummy `.so` to verify `NativeLoader` successfully resolves the `process_frame` symbol.

### Implementation for User Story 1

- [ ] T009 [P] [US1] Create `AlgorithmInfo` model in `app/src/main/java/com/rtkcamera/native/AlgorithmInfo.kt`
- [ ] T010 [US1] Implement Dynamic `.so` loader service in `app/src/main/java/com/rtkcamera/native/NativeLoader.kt`
- [ ] T011 [US1] Implement `process_frame` JNI signature in `app/src/main/cpp/bridge/native_bridge.cpp`
- [ ] T012 [P] [US1] Create "Dummy" C++ plugin implementation in `app/src/main/cpp/plugin/dummy_plugin.cpp`
- [ ] T013 [US1] Unit test for `NativeLoader` in `app/src/test/java/com/rtkcamera/native/NativeLoaderTest.kt`

**Checkpoint**: User Story 1 functional - Native plugins can be loaded and called.

---

## Phase 4: User Story 5 - Standardized Context Delivery (Priority: P1)

**Goal**: Deliver spatial context (orientation, size) to the algorithm via JSON.

**Independent Test**: Verify native parser correctly extracts `width` and `orientation` from the JSON string.

### Implementation for User Story 5

- [ ] T014 [P] [US5] Implement `CameraMetadata` JSON serialization in `app/src/main/java/com/rtkcamera/native/MetadataSerializer.kt`
- [ ] T015 [US5] Implement C++ JSON parser using `nlohmann/json` in `app/src/main/cpp/core/metadata_parser.cpp`
- [ ] T016 [P] [US5] Native GoogleTest for `metadata_parser` in `app/src/main/cpptest/test_metadata.cpp`

**Checkpoint**: User Story 5 functional - Metadata is reliably passed to the native layer.

---

## Phase 5: User Story 2 - Real-Time Evaluation & Feedback (Priority: P1)

**Goal**: Display algorithm output in real-time with performance warnings.

**Independent Test**: Run viewfinder; verify "Low FPS" warning appears when a simulated slow algorithm is active.

### Implementation for User Story 2

- [ ] T017 [US2] Implement `CameraX` ImageAnalysis pipeline in `app/src/main/java/com/rtkcamera/camera/AnalysisPipeline.kt`
- [ ] T018 [US2] Create Compose Viewfinder with algorithm overlay in `app/src/main/java/com/rtkcamera/ui/ViewfinderScreen.kt`
- [ ] T019 [US2] Implement frame-dropping and performance warning logic in `app/src/main/java/com/rtkcamera/ui/CameraViewModel.kt`
- [ ] T020 [US2] Compose UI test for viewfinder in `app/src/androidTest/java/com/rtkcamera/ui/ViewfinderTest.kt`

**Checkpoint**: User Story 2 functional - Real-time processing with feedback is live.

---

## Phase 6: User Story 3 - High-Fidelity Capture & Persistence (Priority: P2)

**Goal**: Trigger and save processed full-resolution snapshots to the gallery.

**Independent Test**: Capture a snapshot and verify it appears in the device's Photo app.

### Implementation for User Story 3

- [ ] T021 [US3] Implement `CameraX` ImageCapture path in `app/src/main/java/com/rtkcamera/camera/CapturePipeline.kt`
- [ ] T022 [US3] Implement `MediaStore` saving logic in `app/src/main/java/com/rtkcamera/camera/GalleryRepository.kt`
- [ ] T023 [US3] Add "Manual Trigger" button to `ViewfinderScreen.kt`
- [ ] T024 [US3] Audit snapshot resolution against sensor capabilities (SC-004)

**Checkpoint**: User Story 3 functional - High-res capture works.

---

## Phase 7: User Story 4 - Algorithm Management & Comparison (Priority: P2)

**Goal**: Switch between different algorithms via the UI.

**Independent Test**: Use the menu to swap between two different plugins; verify the output changes.

### Implementation for User Story 4

- [ ] T025 [P] [US4] Create Algorithm Selection Menu in `app/src/main/java/com/rtkcamera/ui/components/AlgorithmMenu.kt`
- [ ] T026 [US4] Implement algorithm switching logic in `CameraViewModel.kt`

**Checkpoint**: All user stories functional.

---

## Phase 8: Polish & Cross-Cutting Concerns

- [ ] T027 [P] Run all validation scenarios in `quickstart.md`
- [ ] T028 Code cleanup and JNI memory safety audit
- [ ] T029 Performance profiling of `DirectByteBuffer` passing

---

## Dependencies & Execution Order

### Phase Dependencies
- **Phase 1 (Setup)**: Initial blocks.
- **Phase 2 (Foundational)**: Depends on Phase 1. Blocks all stories.
- **Phase 3 (US1)**, **Phase 4 (US5)**: Can proceed in parallel after Foundation.
- **Phase 5 (US2)**: Depends on US1 and US5 completion.
- **Phase 6 (US3)**, **Phase 7 (US4)**: Depend on US2 (UI context).

### Parallel Opportunities
- T003 (NDK Setup) and T002 (Gradle Setup) can run together.
- T005, T006, T007 (DI, Bridge, GTest) can run together.
- T012 (Dummy plugin) can be developed in parallel with T010 (NativeLoader).
- T014 (Kotlin Metadata) and T015 (C++ Metadata) can run in parallel.
