
package com.example.mobileapplicationproject.data.remote.dto

data class InternshipListResponse(
    val status: String,
    val count: Int,
    val internships: List<InternshipDto>
)
