#ifndef METADATA_PARSER_H
#define METADATA_PARSER_H

#include <string>

/**
 * Native representation of CameraMetadata.
 * Matches the JSON contract and Data Model.
 */
struct CameraMetadata {
    int width;
    int height;
    int stride;
    std::string format;
    int orientation;
    long long timestamp;
};

/**
 * Parses a JSON string into a CameraMetadata struct.
 * Uses nlohmann/json for robust parsing.
 */
CameraMetadata parse_metadata(const std::string& json_str);

#endif // METADATA_PARSER_H
