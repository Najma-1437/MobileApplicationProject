
package com.example.mobileapplicationproject.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapplicationproject.data.model.Job
import com.example.mobileapplicationproject.data.repository.JobRepository
import com.example.mobileapplicationproject.data.repository.MockJobRepository
import com.example.mobileapplicationproject.data.sample.SampleJobs
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: JobRepository =
        MockJobRepository(SampleJobs.jobs)
) : ViewModel() {

    var jobs by mutableStateOf<List<Job>>(emptyList())
        private set

    var searchQuery by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadJobs()
    }


    fun loadJobs() {
        if (isLoading) return

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val loadedJobs = repository.getJobs()

                val previousJobs = jobs.associateBy { it.id }

                jobs = loadedJobs.map { newJob ->
                    val previous = previousJobs[newJob.id]

                    if (previous != null) {
                        newJob.copy(
                            isSaved = previous.isSaved,
                            isApplied = previous.isApplied
                        )
                    } else {
                        newJob
                    }
                }

            } catch (exception: CancellationException) {
                throw exception

            } catch (exception: Exception) {
                errorMessage =
                    "Unable to load opportunities. Please try again."

            } finally {
                isLoading = false
            }
        }
    }

    fun retry() {
        loadJobs()
    }

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
            job.title.contains(searchQuery, ignoreCase = true) ||
                    job.company.contains(searchQuery, ignoreCase = true) ||
                    job.skills.any { skill ->
                        skill.contains(searchQuery, ignoreCase = true)
                    }
        }

    val savedJobs: List<Job>
        get() = jobs.filter { it.isSaved }

    val appliedJobs: List<Job>
        get() = jobs.filter { it.isApplied }
}
