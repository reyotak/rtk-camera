# Live Camera Preview Validation Quickstart

This guide provides steps to manually validate that the live camera preview feature works end-to-end on a physical device.

## Prerequisites
- A physical Android device running API 24+ (Emulators often lack proper camera support or provide generic mock feeds).
- Developer mode and USB debugging enabled on the device.

## Setup and Run
1. Connect the physical device to your machine.
2. Build and install the application:
   ```bash
   ./gradlew installDebug
   ```
3. Launch the "RTK Camera" application on the device.

## Validation Scenarios

### Scenario 1: Immediate Feed Visualization
1. Grant the camera permission when prompted by the app.
2. **Expected Outcome**: The viewfinder immediately displays a live video stream from the back camera within 1 second.

### Scenario 2: Overlay Integration
1. Observe the screen while the preview is active.
2. **Expected Outcome**: Any UI controls (buttons, text overlays) are fully visible over the live feed. Tapping them triggers their expected actions without the camera freezing or lagging.

### Scenario 3: Orientation Adaptation
1. Rotate the device from portrait to landscape orientation.
2. **Expected Outcome**: The camera feed rotates to match the physical orientation. The aspect ratio remains correct, without any unnatural stretching or distortion.
