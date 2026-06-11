cmake_minimum_required(VERSION 3.22.1)
project(rtkcamera_test)

set(CMAKE_CXX_STANDARD 17)

# Fetch GTest
include(FetchContent)
FetchContent_Declare(
  googletest
  URL https://github.com/google/googletest/archive/refs/tags/v1.14.0.zip
)
FetchContent_Declare(
  json
  URL https://github.com/nlohmann/json/releases/download/v3.11.2/json.tar.xz
)
FetchContent_MakeAvailable(googletest json)

# Include directories
include_directories(../main/cpp)

# Add test executable
add_executable(run_tests
        test_metadata.cpp
        ../main/cpp/core/metadata_parser.cpp
)

# Link GTest and JSON
target_link_libraries(run_tests
        gtest_main
        nlohmann_json::nlohmann_json
)

# Enable testing
include(GoogleTest)
gtest_discover_tests(run_tests)
