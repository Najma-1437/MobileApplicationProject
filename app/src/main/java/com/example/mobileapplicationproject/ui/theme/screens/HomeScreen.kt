
package com.example.mobileapplicationproject.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mobileapplicationproject.data.model.Job
import com.example.mobileapplicationproject.components.JobCard
import com.example.mobileapplicationproject.viewmodel.HomeViewModel

private val Navy = Color(0xFF17263F)
private val Blue = Color(0xFF2563EB)
private val Cyan = Color(0xFF06B6D4)
private val Background = Color(0xFFF5F7FC)
private val MutedText = Color(0xFF64748B)

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onJobClick: (Int) -> Unit
) {
    val jobs = viewModel.filteredJobs
        .sortedByDescending { it.matchScore }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Navy)
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Good morning,",
                        color = Color(0xFFCBD5E1),
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "Alex Chen",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Cyan
                ) {
                    Text(
                        text = "AC",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = viewModel.searchQuery,
                onValueChange = viewModel::updateSearch,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = null,
                        tint = Color.LightGray
                    )
                },
                placeholder = {
                    Text("Search internships, companies...")
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Cyan,
                    unfocusedBorderColor = Color(0xFF52627A),
                    focusedPlaceholderColor = Color.LightGray,
                    unfocusedPlaceholderColor = Color.LightGray,
                    focusedContainerColor = Color(0xFF293A53),
                    unfocusedContainerColor = Color(0xFF293A53)
                )
            )
        }

        // Opportunities
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SummarySection(viewModel.jobs)
            }

            item {
                Column {
                    Text(
                        text = "Your AI Matches",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Navy
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "Opportunities ranked for your profile",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedText
                    )
                }
            }

            if (jobs.isEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "No opportunities found. Try another search.",
                            modifier = Modifier.padding(20.dp),
                            color = MutedText
                        )
                    }
                }
            }

            items(jobs, key = { it.id }) { job ->
                JobCard(
                    job = job,
                    onBreakdownClick = {
                        onJobClick(job.id)
                    },
                    onSaveClick = {
                        viewModel.toggleSaved(job.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun SummarySection(jobs: List<Job>) {
    val average = if (jobs.isEmpty()) {
        0
    } else {
        jobs.map { it.matchScore }.average().toInt()
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SummaryItem(
            value = "${jobs.size}",
            label = "Matches",
            modifier = Modifier.weight(1f)
        )

        SummaryItem(
            value = "$average%",
            label = "Avg. score",
            modifier = Modifier.weight(1f)
        )

        SummaryItem(
            value = "${jobs.count { it.isSaved }}",
            label = "Saved",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SummaryItem(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                color = Blue,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = label,
                color = MutedText,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
