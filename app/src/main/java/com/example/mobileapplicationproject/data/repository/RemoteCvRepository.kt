
package com.example.mobileapplicationproject.data.repository

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import com.example.mobileapplicationproject.data.remote.CvUploadApiService
import com.example.mobileapplicationproject.data.remote.dto.CvUploadResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RemoteCvRepository(
    private val contentResolver: ContentResolver,
    private val apiService: CvUploadApiService
) : CvRepository {

    override suspend fun uploadCv(
        studentId: Int,
        uri: Uri
    ): CvUploadResponse = withContext(Dispatchers.IO) {

        val mimeType = contentResolver.getType(uri)
            ?: "application/octet-stream"

        val filename = getFileName(uri)

        val bytes = contentResolver.openInputStream(uri)?.use {
            it.readBytes()
        } ?: throw IllegalStateException(
            "Unable to read the selected CV."
        )

        val requestBody = RequestBody.create(
            MediaType.parse(mimeType),
            bytes
        )

        val filePart = MultipartBody.Part.createFormData(
            "file",
            filename,
            requestBody
        )

        apiService.uploadCv(
            studentId = studentId,
            file = filePart
        )
    }

    private fun getFileName(uri: Uri): String {
        contentResolver.query(
            uri,
            arrayOf(OpenableColumns.DISPLAY_NAME),
            null,
            null,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(
                    OpenableColumns.DISPLAY_NAME
                )

                if (index >= 0) {
                    return cursor.getString(index)
                }
            }
        }

        return "student_cv"
    }
}
