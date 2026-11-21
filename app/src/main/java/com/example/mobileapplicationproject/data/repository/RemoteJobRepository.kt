
package com.example.mobileapplicationproject.data.repository

import com.example.mobileapplicationproject.data.model.Job
import com.example.mobileapplicationproject.data.remote.ApiService

class RemoteJobRepository(
    private val apiService: ApiService
) : JobRepository {

    override suspend fun getJobs(): List<Job> {

        val response = apiService.getInternships()

        return response.internships.map { internship ->

            Job(
                id = internship.id,
                title = internship.title,
                company = internship.organization,
                location = internship.location ?: "Not specified",
                type = "Internship",
                salary = "Not specified",
                matchScore = null,
                posted = "Date not available",
                skills = (
                        internship.requiredSkills +
                                internship.preferredSkills
                        ).distinct()
            )
        }
    }
}
