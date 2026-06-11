# Tasks: Live Camera Preview

**Input**: Design documents from `/specs/002-viewfinder-preview/`

**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/CameraManager.md

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic dependencies

- [x] T001 [P] Add CameraX dependencies (`camera-core`, `camera-camera2`, `camera-lifecycle`, `camera-view`) to `app/build.gradle.kts`
- [x] T002 [P] Update `app/src/main/AndroidManifest.xml` to include `<uses-permission android:name="android.permission.CAMERA" />`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [x] T003 Create `CameraManager` interface and basic structure in `app/src/main/java/com/rtk/camera/hardware/CameraManager.kt`
- [x] T004 Create `CameraViewModel` structure for holding permission and provider state in `app/src/main/java/com/rtk/camera/ui/viewmodels/CameraViewModel.kt`

**Checkpoint**: Foundation ready - user story implementation can now begin

---

## Phase 3: User Story 1 - Immediate Live Feed Visualization (Priority: P1) 🎯 MVP

**Goal**: Upon opening the application, the viewfinder must immediately display a live visual stream from the back camera.

**Independent Test**: Can be fully tested by opening the application and observing if a live camera feed appears in the viewfinder without any additional user input.

### Implementation for User Story 1

- [x] T005 [P] [US1] Implement `getCameraProvider` in `app/src/main/java/com/rtk/camera/hardware/CameraManager.kt`
- [x] T006 [US1] Implement permission checking and provider acquisition logic in `app/src/main/java/com/rtk/camera/ui/viewmodels/CameraViewModel.kt`
- [x] T007 [US1] Implement `PreviewView` initialization and `Preview` usecase binding using `AndroidView` inside `app/src/main/java/com/rtk/camera/ui/components/ViewfinderScreen.kt`

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently. The camera preview should display when the screen is opened.

---

## Phase 4: User Story 2 - Interactive Overlay Visibility (Priority: P1)

**Goal**: The active preview must serve as the background, rendering behind all interactive UI controls without blocking their visibility or interaction.

**Independent Test**: Can be tested by ensuring all UI elements render visibly on top of the active preview and are fully tappable/interactive.

### Implementation for User Story 2

- [x] T008 [US2] Update `app/src/main/java/com/rtk/camera/ui/components/ViewfinderScreen.kt` to use a `Box` layout, rendering `AndroidView` as the base layer behind interactive UI overlays
- [x] T009 [US2] Adjust z-ordering or hierarchy in `app/src/main/java/com/rtk/camera/ui/components/ViewfinderScreen.kt` to ensure buttons and menus are fully interactive

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently.

---

## Phase 5: User Story 3 - Orientation and Aspect Ratio Adaptation (Priority: P2)

**Goal**: The preview stream must adapt gracefully to device orientation changes without stretching, distorting, or cropping.

**Independent Test**: Can be tested by rotating the device while the preview is active and observing the preview's dimensions and image scale.

### Implementation for User Story 3

- [x] T010 [US3] Update `app/src/main/java/com/rtk/camera/ui/components/ViewfinderScreen.kt` to set `PreviewView.scaleType` to `FILL_CENTER`

**Checkpoint**: All user stories should now be independently functional.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [x] T011 [P] Handle camera provider binding failures or missing permissions gracefully with fallback UI in `app/src/main/java/com/rtk/camera/ui/components/ViewfinderScreen.kt`
- [x] T012 Run `quickstart.md` manual validation steps to ensure successful end-to-end functionality

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3+)**: All depend on Foundational phase completion
  - User stories can proceed sequentially (US1 → US2 → US3)
- **Polish (Final Phase)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational (Phase 2)
- **User Story 2 (P1)**: Can start after US1, or concurrently, but depends on the base Viewfinder component.
- **User Story 3 (P2)**: Can start after US1 is established.

### Parallel Opportunities

- T001 and T002 in Setup can run in parallel.
- T005 and T006 can be initiated in parallel once foundational files exist.

---

## Parallel Example: User Story 1

```bash
# Launch implementation of the manager and viewmodel together:
Task: "Implement getCameraProvider in app/src/main/java/com/rtk/camera/hardware/CameraManager.kt"
Task: "Implement permission checking and provider acquisition logic in app/src/main/java/com/rtk/camera/ui/viewmodels/CameraViewModel.kt"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational (CRITICAL)
3. Complete Phase 3: User Story 1
4. **STOP and VALIDATE**: Test User Story 1 independently.

### Incremental Delivery

1. Complete Setup + Foundational.
2. Add User Story 1 → Test independently (MVP!).
3. Add User Story 2 → Test independently.
4. Add User Story 3 → Test independently.
