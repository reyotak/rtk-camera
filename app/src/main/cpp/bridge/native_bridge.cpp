#include <jni.h>
#include <string>
#include <android/log.h>
#include <dlfcn.h>
#include "contracts/plugin_contract.h"

#define TAG "NativeBridge"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)

// Function pointer type for the plugin contract
typedef int (*process_frame_ptr)(uint8_t*, const char*);

// Global state for the active plugin
static void* g_plugin_handle = nullptr;
static process_frame_ptr g_process_frame = nullptr;

extern "C" JNIEXPORT jboolean JNICALL
Java_com_rtkcamera_nativebridge_NativeBridge_nativeLoadPlugin(
        JNIEnv* env,
        jobject /* this */,
        jstring path) {
    
    // Close previous plugin if open
    if (g_plugin_handle != nullptr) {
        dlclose(g_plugin_handle);
        g_plugin_handle = nullptr;
        g_process_frame = nullptr;
    }

    const char* native_path = env->GetStringUTFChars(path, nullptr);
    if (native_path == nullptr) return JNI_FALSE;

    // Load the shared library
    g_plugin_handle = dlopen(native_path, RTLD_NOW);
    env->ReleaseStringUTFChars(path, native_path);

    if (g_plugin_handle == nullptr) {
        LOGE("Failed to dlopen: %s", dlerror());
        return JNI_FALSE;
    }

    // Resolve the process_frame symbol
    g_process_frame = (process_frame_ptr)dlsym(g_plugin_handle, "process_frame");
    if (g_process_frame == nullptr) {
        LOGE("Failed to find process_frame: %s", dlerror());
        dlclose(g_plugin_handle);
        g_plugin_handle = nullptr;
        return JNI_FALSE;
    }

    LOGD("Successfully loaded plugin");
    return JNI_TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_rtkcamera_nativebridge_NativeBridge_nativeProcessFrame(
        JNIEnv* env,
        jobject /* this */,
        jobject buffer,
        jstring metadata) {
    
    if (g_process_frame == nullptr) {
        // No plugin loaded, skip processing
        return JNI_FALSE;
    }

    // Get direct buffer address (Zero-copy)
    void* frame_data = env->GetDirectBufferAddress(buffer);
    if (frame_data == nullptr) {
        LOGE("Failed to get direct buffer address");
        return JNI_FALSE;
    }

    // Get metadata JSON
    const char* metadata_json = env->GetStringUTFChars(metadata, nullptr);
    if (metadata_json == nullptr) return JNI_FALSE;

    // Call the plugin contract
    int result = g_process_frame(static_cast<uint8_t*>(frame_data), metadata_json);

    env->ReleaseStringUTFChars(metadata, metadata_json);

    return (result == 0) ? JNI_TRUE : JNI_FALSE;
}
