
package com.example.mobileapplicationproject.ui.theme.screens

import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val Navy = Color(0xFF17263F)
private val Blue = Color(0xFF2563EB)
private val Cyan = Color(0xFF06B6D4)
private val PageBg = Color(0xFFF5F7FC)
private val Muted = Color(0xFF64748B)

@Composable
fun ProfileScreen() {
    val context = LocalContext.current

    var studentName by remember { mutableStateOf("Alex Chen") }
    var course by remember {
        mutableStateOf("Computer Science Student")
    }
    var university by remember {
        mutableStateOf("Strathmore University")
    }

    var showEditDialog by remember { mutableStateOf(false) }
    var showSkillDialog by remember { mutableStateOf(false) }
    var newSkill by remember { mutableStateOf("") }

    val skills = remember {
        mutableStateListOf(
            "Kotlin", "Python", "SQL", "Git"
        )
    }

    var cvName by remember { mutableStateOf<String?>(null) }
    var cvUri by remember { mutableStateOf<android.net.Uri?>(null) }

    val cvPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            cvUri = uri

            // Retrieve the actual display name
            var selectedName: String? = null

            try {
                context.contentResolver.query(
                    uri,
                    arrayOf(OpenableColumns.DISPLAY_NAME),
                    null,
                    null,
                    null
                )?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        selectedName = cursor.getString(0)
                    }
                }
            } catch (_: Exception) {
                selectedName = null
            }

            cvName = selectedName
                ?: uri.lastPathSegment
                        ?: "Selected CV"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg)
            .verticalScroll(rememberScrollState())
    ) {
        // Profile header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Navy)
                .padding(20.dp)
        ) {
            Text(
                "My Profile",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(22.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Cyan,
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        text = studentName
                            .split(" ")
                            .filter { it.isNotBlank() }
                            .take(2)
                            .map { it.first().uppercase() }
                            .joinToString(""),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(18.dp)
                    )
                }

                Spacer(Modifier.width(14.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        studentName,
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        course,
                        color = Color(0xFFCBD5E1),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        university,
                        color = Color(0xFFCBD5E1),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                IconButton(
                    onClick = { showEditDialog = true }
                ) {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = "Edit profile",
                        tint = Color.White
                    )
                }
            }
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // CV section
            ProfileCard("My Resume") {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.Description,
                        contentDescription = null,
                        tint = Blue,
                        modifier = Modifier.size(32.dp)
                    )

                    Spacer(Modifier.width(12.dp))

                    Column(Modifier.weight(1f)) {
                        Text(
                            cvName ?: "No CV selected",
                            color = Navy,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            if (cvUri == null)
                                "PDF or Word document"
                            else
                                "Selected on this device",
                            color = Muted,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                Text(
                    "Choose your CV to prepare for personalized " +
                            "internship matching and application advice.",
                    color = Muted
                )

                Spacer(Modifier.height(14.dp))

                Button(
                    onClick = {
                        cvPicker.launch(
                            arrayOf(
                                "application/pdf",
                                "application/vnd.openxmlformats-" +
                                        "officedocument.wordprocessingml.document",
                                "application/msword"
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue
                    )
                ) {
                    Icon(
                        Icons.Outlined.UploadFile,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        if (cvUri == null) "Select CV"
                        else "Change CV"
                    )
                }

                if (cvUri != null) {
                    TextButton(
                        onClick = {
                            cvUri = null
                            cvName = null
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Remove selected CV")
                    }

                    Text(
                        "CV selected successfully. Analysis and " +
                                "server upload are not connected yet.",
                        color = Muted,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Skills section
            ProfileCard("Technical Skills") {
                Text(
                    "Add skills you have learned or used in projects.",
                    color = Muted,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(Modifier.height(12.dp))

                skills.forEach { skill ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = Color(0xFFE8F0FF),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                skill,
                                color = Blue,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(12.dp)
                            )
                        }

                        IconButton(
                            onClick = { skills.remove(skill) }
                        ) {
                            Icon(
                                Icons.Outlined.Close,
                                contentDescription = "Remove $skill",
                                tint = Muted
                            )
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                OutlinedButton(
                    onClick = { showSkillDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Outlined.Add,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Add Skill")
                }
            }

            // Application readiness
            ProfileCard("Application Readiness") {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.AutoAwesome,
                        contentDescription = null,
                        tint = Blue
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "AI Career Assistant",
                        color = Navy,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(Modifier.height(10.dp))

                Text(
                    "After CV analysis is connected, this section " +
                            "will display CV feedback, skill gaps, " +
                            "and tailored application recommendations.",
                    color = Muted
                )
            }
        }
    }

    // Edit profile dialog
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Profile") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = studentName,
                        onValueChange = { studentName = it },
                        label = { Text("Full name") },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = course,
                        onValueChange = { course = it },
                        label = { Text("Course") },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = university,
                        onValueChange = { university = it },
                        label = { Text("University") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showEditDialog = false }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showEditDialog = false }
                ) {
                    Text("Close")
                }
            }
        )
    }

    // Add skill dialog
    if (showSkillDialog) {
        AlertDialog(
            onDismissRequest = {
                showSkillDialog = false
                newSkill = ""
            },
            title = { Text("Add Technical Skill") },
            text = {
                OutlinedTextField(
                    value = newSkill,
                    onValueChange = { newSkill = it },
                    label = { Text("Skill name") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val value = newSkill.trim()
                        if (value.isNotEmpty() &&
                            skills.none {
                                it.equals(value, ignoreCase = true)
                            }
                        ) {
                            skills.add(value)
                        }
                        newSkill = ""
                        showSkillDialog = false
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        newSkill = ""
                        showSkillDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ProfileCard(
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
