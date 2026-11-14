
package com.example.mobileapplicationproject.data.remote.dto

data class CvUploadResponse(
    val cv_id: Int,
    val filename: String,
    val status: String,
    val message: String? = null
)
