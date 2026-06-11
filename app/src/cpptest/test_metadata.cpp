#include <gtest/gtest.h>
#include "core/metadata_parser.h"
#include <nlohmann/json.hpp>

/**
 * GTest for metadata_parser.cpp.
 * Verifies that the native layer correctly extracts context from the JSON contract.
 */

TEST(MetadataParserTest, ParsesValidJson) {
    std::string json_str = R"({
        "width": 1920,
        "height": 1080,
        "stride": 1920,
        "format": "RGBA_8888",
        "orientation": 90,
        "timestamp": 123456789
    })";

    CameraMetadata meta = parse_metadata(json_str);

    EXPECT_EQ(meta.width, 1920);
    EXPECT_EQ(meta.height, 1080);
    EXPECT_EQ(meta.stride, 1920);
    EXPECT_EQ(meta.format, "RGBA_8888");
    EXPECT_EQ(meta.orientation, 90);
    EXPECT_EQ(meta.timestamp, 123456789);
}

TEST(MetadataParserTest, ThrowsOnMissingKey) {
    std::string json_str = R"({"width": 1920})";
    // nlohmann::json::at() throws out_of_range if key is missing
    EXPECT_THROW(parse_metadata(json_str), nlohmann::json::out_of_range);
}

TEST(MetadataParserTest, ThrowsOnInvalidJson) {
    std::string json_str = R"({ "width": 1920, )"; // Malformed
    EXPECT_THROW(parse_metadata(json_str), nlohmann::json::parse_error);
}
