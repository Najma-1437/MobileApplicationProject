package com.example.mobileapplicationproject.data.remote

import com.example.mobileapplicationproject.data.remote.dto.InternshipListResponse
import com.example.mobileapplicationproject.data.remote.dto.InternshipDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("api/v1/internships")
    suspend fun getInternships(): InternshipListResponse

    @GET("api/v1/internships/{internship_id}")
    suspend fun getInternship(
        @Path("internship_id") internshipId: String
    ): InternshipDetailResponse
}