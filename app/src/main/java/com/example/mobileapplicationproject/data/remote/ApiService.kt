
package com.example.mobileapplicationproject.data.remote

import com.example.mobileapplicationproject.data.remote.dto.JobDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("api/jobs")
    suspend fun getJobs(): List<JobDto>

    @GET("api/jobs/{id}")
    suspend fun getJob(
        @Path("id") id: Int
    ): JobDto
}
