package com.example.mobileapplicationproject.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobileapplicationproject.data.model.Job
import com.example.mobileapplicationproject.components.JobCard

@Composable
fun OpportunityListScreen(
    title: String,
    jobs: List<Job>,
    onJobClick: (Int) -> Unit,
    onSaveClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            title,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (jobs.isEmpty()) {
                item {
                    Text("No opportunities to display.")
                }
            }

            items(jobs, key = { it.id }) { job ->
                JobCard(
                    job = job,
                    onBreakdownClick = {
                        onJobClick(job.id)
                    },
                    onSaveClick = {
                        onSaveClick(job.id)
                    }
                )
            }
        }
    }
}
