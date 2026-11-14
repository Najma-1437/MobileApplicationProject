
package com.example.mobileapplicationproject.data.remote

import com.example.mobileapplicationproject.data.remote.dto.CvUploadResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface CvUploadApiService {

    @Multipart
    @POST("api/students/{studentId}/cv")
    suspend fun uploadCv(
        @Path("studentId") studentId: Int,
        @Part file: MultipartBody.Part
    ): CvUploadResponse
}
