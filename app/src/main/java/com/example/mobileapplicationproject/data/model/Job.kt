
package com.example.mobileapplicationproject.data.model

data class Job(
    val id: Int,
    val title: String,
    val company: String,
    val location: String,
    val type: String,
    val salary: String,
    val matchScore: Int? = null,
    val posted: String = "Date not available",
    val skills: List<String>,
    val isSaved: Boolean = false,
    val isApplied: Boolean = false
)
