package com.rtkcamera.camera

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository to handle image persistence to the system gallery.
 */
@Singleton
class GalleryRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /**
     * Saves a byte array as a JPEG image in the MediaStore.
     */
    fun saveImage(data: ByteArray, fileName: String) {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        try {
            val uri = context.contentResolver.insert(collection, values)
            uri?.let { targetUri ->
                context.contentResolver.openOutputStream(targetUri)?.use { stream ->
                    stream.write(data)
                }
                
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    values.clear()
                    values.put(MediaStore.Images.Media.IS_PENDING, 0)
                    context.contentResolver.update(targetUri, values, null, null)
                }
                Log.d(TAG, "Image saved successfully: $targetUri")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save image to gallery", e)
        }
    }

    companion object {
        private const val TAG = "GalleryRepository"
    }
}
