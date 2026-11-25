
package com.example.mobileapplicationproject.data.remote.dto

import com.google.gson.annotations.SerializedName

data class InternshipDto(

    val id: String,

    val title: String,

    val organization: String,

    val description: String,

    @SerializedName("required_skills")
    val requiredSkills: List<String>,

    @SerializedName("preferred_skills")
    val preferredSkills: List<String>,

    @SerializedName("education_requirements")
    val educationRequirements: List<String>,

    @SerializedName("minimum_experience_months")
    val minimumExperienceMonths: Int,

    @SerializedName("eligibility_criteria")
    val eligibilityCriteria: List<String>,

    val location: String?,

    @SerializedName("work_arrangement")
    val workArrangement: String,

    @SerializedName("application_deadline")
    val applicationDeadline: String?,

    @SerializedName("application_url")
    val applicationUrl: String?,

    @SerializedName("is_active")
    val isActive: Boolean
)
