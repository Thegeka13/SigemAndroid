package com.geka.sigem.data.remote

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun uriToFile(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)
    val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)

    FileOutputStream(tempFile).use { output ->
        inputStream?.copyTo(output)
    }

    return tempFile
}