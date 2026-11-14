
package com.example.mobileapplicationproject.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.mobileapplicationproject.data.model.Job
import com.example.mobileapplicationproject.data.sample.SampleJobs

class HomeViewModel : ViewModel() {

    var jobs by mutableStateOf(SampleJobs.jobs)
        private set

    var searchQuery by mutableStateOf("")
        private set

    fun updateSearch(query: String) {
        searchQuery = query
    }

    fun toggleSaved(id: Int) {
        jobs = jobs.map { job ->
            if (job.id == id) {
                job.copy(isSaved = !job.isSaved)
            } else {
                job
            }
        }
    }

    val filteredJobs: List<Job>
        get() = jobs.filter { job ->
            job.title.contains(searchQuery, true) ||
                    job.company.contains(searchQuery, true) ||
                    job.skills.any {
                        it.contains(searchQuery, true)
                    }
        }

    val savedJobs: List<Job>
        get() = jobs.filter { it.isSaved }

    val appliedJobs: List<Job>
        get() = jobs.filter { it.isApplied }
}
