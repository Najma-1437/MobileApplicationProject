
package com.example.mobileapplicationproject.data.remote.dto

data class CvUploadResponse(
    val status: String,
    val message: String,
    val filename: String,
    val profile: Map<String, Any>
)
