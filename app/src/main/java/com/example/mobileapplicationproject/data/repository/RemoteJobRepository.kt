
package com.example.mobileapplicationproject.data.repository

import com.example.mobileapplicationproject.data.model.Job
import com.example.mobileapplicationproject.data.remote.ApiService

class RemoteJobRepository(
    private val apiService: ApiService
) : JobRepository {

    override suspend fun getJobs(): List<Job> {

        val response = apiService.getJobs()

        return response.map { jobDto ->
            Job(
                id = jobDto.id,
                title = jobDto.title,
                company = jobDto.company,
                location = jobDto.location,
                type = jobDto.type,
                salary = jobDto.salary,
                skills = jobDto.skills,
                matchScore = null,
                posted = "Date not available"
            )
        }
    }
}
