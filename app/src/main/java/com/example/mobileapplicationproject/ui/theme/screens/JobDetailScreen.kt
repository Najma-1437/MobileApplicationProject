
package com.example.mobileapplicationproject.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mobileapplicationproject.data.model.Job

private val Navy = Color(0xFF17263F)
private val Blue = Color(0xFF2563EB)
private val Cyan = Color(0xFF06B6D4)
private val PageBg = Color(0xFFF5F7FC)
private val Muted = Color(0xFF64748B)

@Composable
fun JobDetailScreen(
    job: Job?,
    onBack: () -> Unit
) {
    var showCoach by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Navy)
                .padding(20.dp)
        ) {
            TextButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(Modifier.width(8.dp))
                Text("Back", color = Color.White)
            }

            Spacer(Modifier.height(12.dp))

            Text(
                "AI MATCH ANALYSIS",
                color = Cyan,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Text(
                job?.title ?: "Opportunity not found",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            if (job != null) {
                Spacer(Modifier.height(6.dp))
                Text(
                    "${job.company} • ${job.location}",
                    color = Color(0xFFCBD5E1)
                )
            }
        }

        if (job == null) {
            Text(
                "This opportunity could not be found.",
                modifier = Modifier.padding(20.dp)
            )
            return@Column
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Match score
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                "Your Match Score",
                                color = Muted,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                "${job.matchScore}%",
                                color = Blue,
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Icon(
                            Icons.Outlined.AutoAwesome,
                            contentDescription = null,
                            tint = Blue,
                            modifier = Modifier.size(38.dp)
                        )
                    }

                    LinearProgressIndicator(
                        progress = {
                            (job.matchScore / 100f).coerceIn(0f, 1f)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp),
                        color = Blue,
                        trackColor = Color(0xFFE2E8F0)
                    )

                    Text(
                        "Demo score only. Upload a CV and connect " +
                                "the analysis backend for a real assessment.",
                        color = Muted,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            // Opportunity details
            DetailCard("Opportunity Overview") {
                DetailRow("Company", job.company)
                DetailRow("Location", job.location)
                DetailRow("Work type", job.type)
                DetailRow("Compensation", job.salary)
            }

            // Skills
            DetailCard("Required Skills") {
                Text(
                    "Skills listed for this opportunity",
                    color = Muted,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(Modifier.height(10.dp))

                job.skills.forEach { skill ->
                    Surface(
                        color = Color(0xFFE8F0FF),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 9.dp
                            )
                        ) {
                            Icon(
                                Icons.Outlined.Code,
                                contentDescription = null,
                                tint = Blue,
                                modifier = Modifier.size(17.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(skill, color = Navy)
                        }
                    }
                }
            }

            // Analysis status
            DetailCard("CV Analysis Status") {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = null,
                        tint = Color(0xFFD97706)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "Awaiting CV analysis",
                        fontWeight = FontWeight.SemiBold,
                        color = Navy
                    )
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    "We cannot determine which requirements " +
                            "you meet or which skills are missing " +
                            "until your CV has been analyzed.",
                    color = Muted
                )
            }

            // Coach
            Button(
                onClick = { showCoach = !showCoach },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue
                )
            ) {
                Icon(
                    Icons.Outlined.AutoAwesome,
                    contentDescription = null
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    if (showCoach) "Hide Application Tips"
                    else "Explore Application Coach"
                )
            }

            if (showCoach) {
                DetailCard("Application Coach — Preview") {
                    Text(
                        "General preparation tips",
                        fontWeight = FontWeight.SemiBold,
                        color = Navy
                    )

                    Spacer(Modifier.height(10.dp))

                    CoachingTip(
                        "1. Review the requirements",
                        "Compare the listed skills with your " +
                                "actual experience and projects."
                    )

                    CoachingTip(
                        "2. Tailor your CV",
                        "Highlight relevant experience you " +
                                "genuinely have. Do not add skills " +
                                "or achievements you cannot verify."
                    )

                    CoachingTip(
                        "3. Show evidence",
                        "Describe projects using specific " +
                                "technologies, your contribution, " +
                                "and measurable results where available."
                    )

                    CoachingTip(
                        "4. Prepare for interviews",
                        "Practice explaining how your projects " +
                                "relate to the opportunity."
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        "These are general tips, not " +
                                "AI-generated CV recommendations.",
                        color = Muted,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun DetailCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                title,
                color = Navy,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(14.dp))
            content()
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            color = Muted,
            modifier = Modifier.weight(1f)
        )
        Text(
            value,
            color = Navy,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1.3f)
        )
    }
}

@Composable
private fun CoachingTip(
    title: String,
    description: String
) {
    Column(
        modifier = Modifier.padding(bottom = 14.dp)
    ) {
        Text(
            title,
            color = Blue,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(4.dp))
        Text(description, color = Muted)
    }
}
