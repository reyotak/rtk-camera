/**
 * RTK Camera Algorithm Plugin Contract
 * Standardized C Header for external algorithm logic.
 */

#ifndef RTK_CAMERA_PLUGIN_H
#define RTK_CAMERA_PLUGIN_H

#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Processes a single frame.
 * @param frame_data Pointer to the DirectByteBuffer memory.
 * @param metadata_json JSON-encoded string containing CameraMetadata.
 * @return 0 on success, error code otherwise.
 */
int process_frame(uint8_t* frame_data, const char* metadata_json);

#ifdef __cplusplus
}
#endif

#endif // RTK_CAMERA_PLUGIN_H
