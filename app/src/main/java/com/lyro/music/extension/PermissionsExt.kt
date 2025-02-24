package com.lyro.music.extension

import android.Manifest
import android.os.Build

/**
 * Check for android 13 (TIRAMISU) permissions
 */
fun getReadStoragePermission() = when {
    Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
         -> Manifest.permission.READ_EXTERNAL_STORAGE
    else -> Manifest.permission.READ_MEDIA_AUDIO
}