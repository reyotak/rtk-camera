#include "../contracts/plugin_contract.h"
#include <string.h>

/**
 * Dummy plugin implementation for testing the sandbox infrastructure.
 * Simply returns success (0) without modifying the frame.
 */
extern "C" int process_frame(uint8_t* frame_data, const char* metadata_json) {
    // Future work: parse metadata_json to get width/height and apply a filter (e.g. grayscale)
    return 0; 
}
