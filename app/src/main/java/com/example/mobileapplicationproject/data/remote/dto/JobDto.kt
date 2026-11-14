
package com.example.mobileapplicationproject.data.remote.dto

data class JobDto(
    val id: Int,
    val title: String,
    val company: String,
    val location: String,
    val type: String,
    val salary: String,
    val skills: List<String>
)
