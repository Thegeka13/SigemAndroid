package com.geka.sigem.data.remote

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

fun String.toRequestBody(): RequestBody =
    RequestBody.create("text/plain".toMediaTypeOrNull(), this)

fun createMultipart(file: File, fieldName: String = "files"): MultipartBody.Part {
    return MultipartBody.Part.createFormData(
        fieldName,
        file.name,
        RequestBody.create("image/*".toMediaTypeOrNull(), file)
    )
}