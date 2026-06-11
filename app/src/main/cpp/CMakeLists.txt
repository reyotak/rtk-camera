cmake_minimum_required(VERSION 3.22.1)

project("rtkcamera")

# Fetch nlohmann/json
include(FetchContent)
FetchContent_Declare(
  json
  URL https://github.com/nlohmann/json/releases/download/v3.11.2/json.tar.xz
)
FetchContent_MakeAvailable(json)

# Include directories
include_directories(.)

# Add main library
add_library(${CMAKE_PROJECT_NAME} SHARED
        bridge/native_bridge.cpp
        core/metadata_parser.cpp
)

# Add dummy plugin library
add_library(dummy_plugin SHARED
        plugin/dummy_plugin.cpp
)

# Find Android log library
find_library(log-lib log)

# Link libraries
target_link_libraries(${CMAKE_PROJECT_NAME}
        ${log-lib}
        nlohmann_json::nlohmann_json
)
