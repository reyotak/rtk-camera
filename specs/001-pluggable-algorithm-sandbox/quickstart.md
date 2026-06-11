# Quickstart: Pluggable Camera Algorithm Sandbox Validation

This guide provides scenarios to validate the Algorithm Sandbox feature end-to-end.

## Prerequisites
- Android device with Camera access (API 24+).
- Compiled `.so` plugin following the [Plugin Contract](contracts/plugin_contract.h).

## Scenario 1: Real-Time Plugin Execution
**Goal**: Verify that a native plugin can process preview frames in real-time.
1. **Setup**: Place `libdummy_plugin.so` in the app's internal library directory.
2. **Action**: Open the RTK Camera app and select "Dummy Plugin" from the algorithm menu.
3. **Observation**: The camera preview displays the processed output (e.g., grayscale).
4. **Validation**: Check that the `performanceWarning` overlay is NOT visible if the processing is fast.

## Scenario 2: Developer Onboarding (SC-001)
**Goal**: Verify that a new algorithm can be integrated and running within 5 minutes.
1. **Action**: Start a timer. Compile the dummy plugin, copy it to the device, and select it in the UI.
2. **Validation**: The timer must be under 5 minutes when the processed preview is first visible.

## Scenario 3: High-Resolution Snapshot
**Goal**: Verify that snapshots are processed at full resolution and saved.
1. **Action**: Press the manual capture button while an algorithm is active.
2. **Observation**: A "Processing..." dialog appears briefly.
3. **Validation**: Open the system Photo Gallery; verify the last image is the processed high-res version.

## Scenario 4: Performance Warning
**Goal**: Verify the system notifies the user of slow algorithms.
1. **Action**: Load a "Heavy Plugin" (simulated 100ms delay).
2. **Observation**: The UI remains responsive (can still press buttons), but a "Low FPS" warning appears on the screen.

## Running Tests
### Kotlin Unit Tests
```bash
./gradlew test
```
### Native GoogleTests
```bash
cd app/src/main/cpptest
cmake . && make && ./run_tests
```
