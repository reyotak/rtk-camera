#include "metadata_parser.h"
#include <nlohmann/json.hpp>

using json = nlohmann::json;

CameraMetadata parse_metadata(const std::string& json_str) {
    auto j = json::parse(json_str);
    
    CameraMetadata meta;
    meta.width = j.at("width").get<int>();
    meta.height = j.at("height").get<int>();
    meta.stride = j.at("stride").get<int>();
    meta.format = j.at("format").get<std::string>();
    meta.orientation = j.at("orientation").get<int>();
    meta.timestamp = j.at("timestamp").get<long long>();
    
    return meta;
}
