
package com.example.mobileapplicationproject.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mobileapplicationproject.data.model.Job

private val Blue = Color(0xFF2563EB)
private val Green = Color(0xFF16A34A)
private val Orange = Color(0xFFF59E0B)

@Composable
fun JobCard(
    job: Job,
    onBreakdownClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    val matchColor = when {
        job.matchScore >= 80 -> Green
        job.matchScore >= 70 -> Orange
        else -> Color(0xFFEF4444)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Blue,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = job.company.first().toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(14.dp)
                    )
                }

                Spacer(Modifier.width(10.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        text = job.title,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = job.company,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                IconButton(onClick = onSaveClick) {
                    Icon(
                        imageVector = if (job.isSaved)
                            Icons.Filled.Bookmark
                        else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Save opportunity",
                        tint = Blue
                    )
                }
            }

            Text(
                text = "${job.type}  •  ${job.location}  •  ${job.posted}",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "● ${job.matchScore}% Match",
                    color = matchColor,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(Modifier.weight(1f))

                Text(
                    text = job.salary,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Button(
                onClick = onBreakdownClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue
                ),
                shape = RoundedCornerShape(9.dp)
            ) {
                Text("View Breakdown")
            }

            if (job.isApplied) {
                Text(
                    text = "✓ Applied",
                    color = Green,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}
