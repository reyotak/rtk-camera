# Tasks: Snapshot Capture

**Input**: Design documents from `/specs/003-snapshot-capture/`

**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: Test tasks are included as requested by the implementation plan.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- **Android App**: `android/app/src/main/java/com/rtkcamera/`

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [x] T001 Verify Android CameraX and MediaStore dependencies in `app/build.gradle`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [x] T002 [P] Create `CapturedImage`, `ProcessedImage`, and `GallerySaveResult` data models in `app/src/main/java/com/rtkcamera/camera/data/Models.kt`
- [x] T003 [P] Define `CapturePipeline` interface in `app/src/main/java/com/rtkcamera/camera/CapturePipeline.kt`
- [x] T004 [P] Define `GalleryRepository` interface in `app/src/main/java/com/rtkcamera/camera/data/GalleryRepository.kt`
- [x] T005 Implement `GalleryRepositoryImpl` using `MediaStore` for asynchronous saving in `app/src/main/java/com/rtkcamera/camera/data/GalleryRepositoryImpl.kt`
- [x] T006 Set up Hilt module to provide `GalleryRepository` and `CapturePipeline` in `app/src/main/java/com/rtkcamera/di/CameraModule.kt`

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Standard Image Capture (Priority: P1) 🎯 MVP

**Goal**: A user taps the snapshot button, a photo is captured and immediately saved to the device's local photo gallery.

**Independent Test**: Can be fully tested by launching the app, tapping the capture button, and verifying a new photo appears in the device's gallery.

### Tests for User Story 1 ⚠️

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [x] T007 [P] [US1] Unit test for `CameraViewModel` trigger logic in `app/src/test/java/com/rtkcamera/ui/CameraViewModelTest.kt`
- [x] T008 [P] [US1] Unit test for `CapturePipeline` mock integration in `app/src/test/java/com/rtkcamera/camera/CapturePipelineTest.kt`
- [x] T009 [P] [US1] UI test for Viewfinder snapshot button trigger in `app/src/androidTest/java/com/rtkcamera/ui/ViewfinderTest.kt`

### Implementation for User Story 1

- [x] T010 [US1] Implement `CapturePipelineImpl` to trigger `takePicture` and save to `GalleryRepository` in `android/app/src/main/java/com/rtkcamera/camera/CapturePipelineImpl.kt`
- [x] T011 [US1] Update `CameraViewModel.kt` to expose an `onCaptureTriggered` event (via StateFlow/SharedFlow) in `android/app/src/main/java/com/rtkcamera/ui/CameraViewModel.kt`
- [x] T012 [US1] Integrate `ImageCapture` use case alongside `Preview` in `ViewfinderScreen.kt` lifecycle-binding block in `android/app/src/main/java/com/rtkcamera/ui/ViewfinderScreen.kt`
- [x] T013 [US1] Connect snapshot button `onClick` listener to `CameraViewModel.onCaptureTriggered()` in `android/app/src/main/java/com/rtkcamera/ui/ViewfinderScreen.kt`

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - Uninterrupted Viewfinder Feed (Priority: P2)

**Goal**: While a photo is being captured, processed, and saved in the background, the live viewfinder preview must remain responsive and smooth.

**Independent Test**: Can be tested by observing the live feed immediately after tapping the capture button to ensure there are no frame rate drops or UI stutters.

### Implementation for User Story 2

- [x] T014 [US2] Update `CapturePipelineImpl` to ensure `takePicture` callback uses `Dispatchers.IO` for frame processing and repository saving, explicitly avoiding the main thread in `android/app/src/main/java/com/rtkcamera/camera/CapturePipelineImpl.kt`
- [x] T015 [US2] Ensure `ViewfinderScreen` handles capture triggered events asynchronously without blocking Compose recomposition in `android/app/src/main/java/com/rtkcamera/ui/ViewfinderScreen.kt`

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently

---

## Phase 5: User Story 3 - Enhanced Image Capture (Priority: P3)

**Goal**: A user selects an active image processing algorithm, taps the snapshot button, the system applies the algorithm to the high-resolution photo, and saves the enhanced result.

**Independent Test**: Can be fully tested by selecting an algorithm, capturing a photo, and verifying the saved image in the gallery has the algorithm applied.

### Implementation for User Story 3

- [x] T016 [US3] Update `CapturePipelineImpl.kt` to extract frame data from `ImageProxy` and pass it to the native bridge (`AlgorithmProcessor`) before saving in `android/app/src/main/java/com/rtkcamera/camera/CapturePipelineImpl.kt`
- [x] T017 [US3] Explicitly add a `finally` block in `CapturePipelineImpl` to guarantee `imageProxy.close()` is called to prevent CameraX memory leaks in `android/app/src/main/java/com/rtkcamera/camera/CapturePipelineImpl.kt`

**Checkpoint**: All user stories should now be independently functional

---

## Phase N: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [ ] T018 Run `quickstart.md` validation scenarios.
- [ ] T019 Update developer documentation in README if new environment properties are needed.

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3+)**: All depend on Foundational phase completion
  - Sequential delivery: US1 -> US2 -> US3

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational (Phase 2)
- **User Story 2 (P2)**: Integrates with US1 logic to ensure off-main-thread execution.
- **User Story 3 (P3)**: Integrates with US1/US2 logic to pass data to algorithms.

### Parallel Opportunities

- T002, T003, T004 can run in parallel.
- All tests for US1 (T007, T008, T009) can run in parallel.

---

## Parallel Example: User Story 1

```bash
# Launch all tests for User Story 1 together (if tests requested):
Task: "Unit test for CameraViewModel trigger logic in CameraViewModelTest.kt"
Task: "Unit test for CapturePipeline mock integration in CapturePipelineTest.kt"
Task: "UI test for Viewfinder snapshot button trigger in ViewfinderScreenTest.kt"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational (CRITICAL - blocks all stories)
3. Complete Phase 3: User Story 1
4. **STOP and VALIDATE**: Test User Story 1 independently
5. Deploy/demo if ready

### Incremental Delivery

1. Complete Setup + Foundational → Foundation ready
2. Add User Story 1 → Test independently → Deploy/Demo (MVP!)
3. Add User Story 2 → Test independently → Deploy/Demo
4. Add User Story 3 → Test independently → Deploy/Demo
5. Each story adds value without breaking previous stories
