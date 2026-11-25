
package com.example.mobileapplicationproject.data.sample

import com.example.mobileapplicationproject.data.model.Job

object SampleJobs {

    val jobs = listOf(
        Job(
            id = "1",
            title = "Junior Android Developer",
            company = "Demo Company A",
            location = "Remote",
            type = "Internship",
            salary = "Stipend not specified",
            matchScore = 88,
            posted = "2 days ago",
            skills = listOf("Kotlin", "Git", "Firebase")
        ),
        Job(
            id = "2",
            title = "Frontend Engineering Intern",
            company = "Demo Company B",
            location = "Nairobi",
            type = "Internship",
            salary = "Stipend not specified",
            matchScore = 76,
            posted = "1 day ago",
            skills = listOf("React", "TypeScript", "Git"),
            isSaved = true
        ),
        Job(
            id = "3",
            title = "Backend Software Engineer",
            company = "Demo Company C",
            location = "Remote",
            type = "Full-time",
            salary = "Not specified",
            matchScore = 62,
            posted = "3 days ago",
            skills = listOf("Python", "SQL", "REST APIs"),
            isApplied = true
        ),
        Job(
            id = "4",
            title = "Data Analytics Intern",
            company = "Demo Company D",
            location = "Nairobi",
            type = "Internship",
            salary = "Stipend not specified",
            matchScore = 81,
            posted = "4 days ago",
            skills = listOf("Python", "SQL", "Excel")
        )
    )
}
