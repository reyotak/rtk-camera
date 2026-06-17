# Quickstart Validation Guide: Snapshot Capture

This guide details how to validate the manual snapshot capture and photo persistence feature end-to-end.

## Prerequisites
- Android device or emulator running API 24 or higher.
- Device/Emulator has available storage space.
- The RTK Camera app is installed and granted Camera and Storage/Media permissions.

## Validation Scenarios

### Scenario 1: Standard Image Capture & Persistence
**Purpose**: Verify the camera captures and saves an image to the device gallery.

1.  **Launch App**: Open the RTK Camera app.
2.  **Ensure Preview Active**: Verify the live camera feed is displaying smoothly on the screen.
3.  **Trigger Capture**: Tap the snapshot/capture button on the UI.
4.  **Verify Smooth UI**: Observe the live feed immediately after tapping; it should not freeze or stutter.
5.  **Verify Persistence**: 
    - Background the RTK Camera app.
    - Open the device's default Photo Gallery or Google Photos app.
    - **Expected Outcome**: A new high-resolution photo from the camera should be present as the most recent item in the gallery.

### Scenario 2: Capture with Algorithm Processing
**Purpose**: Verify that an active algorithm is applied to the full-resolution saved image.

1.  **Launch App**: Open the RTK Camera app.
2.  **Select Algorithm**: Using the UI, select an active image processing algorithm (e.g., a "Grayscale" or "Edge Detection" filter).
3.  **Trigger Capture**: Tap the snapshot/capture button.
4.  **Verify Persistence**: Open the device's Photo Gallery.
5.  **Expected Outcome**: The most recently saved photo should exhibit the visual effects of the selected algorithm.

### Scenario 3: Memory Leak Prevention Verification
**Purpose**: Ensure `ImageProxy` resources are correctly closed after capture.

1.  **Setup**: Connect the device via ADB.
2.  **Action**: Open the app and tap the capture button rapidly 10 times.
3.  **Monitor Logcat**: Monitor the system logs using `adb logcat | grep -i "ImageCapture"`.
4.  **Expected Outcome**: The system should not throw `ImageCapture` buffer exhaustion exceptions, indicating resources are successfully closed in the pipeline's `finally` block.
