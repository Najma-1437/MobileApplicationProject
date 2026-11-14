
package com.example.mobileapplicationproject.ui.theme.screens

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.UploadFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobileapplicationproject.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

private val Blue = Color(0xFF4F6BED)
private val Lavender = Color(0xFFEDE9FE)
private val Background = Color(0xFFF8F9FF)
private val Indigo = Color(0xFF273469)
private val Muted = Color(0xFF69748E)

private const val MAX_CV_BYTES = 5L * 1024L * 1024L

@Composable
fun ProfileScreen(
    studentName: String = "Alex",
    onStudentNameChange: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    profileViewModel: ProfileViewModel = viewModel()
) {
    val context = LocalContext.current
    val resolver = context.contentResolver

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()

    var course by rememberSaveable {
        mutableStateOf("Computer Science")
    }

    var university by rememberSaveable {
        mutableStateOf("Strathmore University")
    }

    var interests by rememberSaveable {
        mutableStateOf(
            "Software Development, Data Analytics"
        )
    }

    var skills by rememberSaveable {
        mutableStateOf("Kotlin, Python, SQL, Git")
    }

    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->

        if (uri == null) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    "CV selection cancelled"
                )
            }
        } else {
            try {
                var filename = "Selected CV"
                var fileSize: Long? = null

                resolver.query(
                    uri,
                    arrayOf(
                        OpenableColumns.DISPLAY_NAME,
                        OpenableColumns.SIZE
                    ),
                    null,
                    null,
                    null
                )?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val nameIndex = cursor.getColumnIndex(
                            OpenableColumns.DISPLAY_NAME
                        )

                        val sizeIndex = cursor.getColumnIndex(
                            OpenableColumns.SIZE
                        )

                        if (nameIndex >= 0) {
                            filename = cursor.getString(nameIndex)
                                ?: filename
                        }

                        if (
                            sizeIndex >= 0 &&
                            !cursor.isNull(sizeIndex)
                        ) {
                            fileSize = cursor.getLong(sizeIndex)
                        }
                    }
                }

                val validExtension = listOf(
                    ".pdf",
                    ".doc",
                    ".docx"
                ).any { extension ->
                    filename.endsWith(
                        extension,
                        ignoreCase = true
                    )
                }

                val mimeType = resolver.getType(uri)

                val validMime = mimeType == null ||
                        mimeType == "application/octet-stream" ||
                        mimeType in setOf(
                    "application/pdf",
                    "application/msword",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                )

                when {
                    !validExtension || !validMime -> {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Choose a PDF, DOC or DOCX file"
                            )
                        }
                    }

                    fileSize != null &&
                            fileSize!! > MAX_CV_BYTES -> {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "CV must be 5 MB or smaller"
                            )
                        }
                    }

                    else -> {
                        profileViewModel.selectCv(
                            uri = uri,
                            name = filename,
                            size = fileSize
                        )
                    }
                }
            } catch (exception: Exception) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        "Could not read the selected file"
                    )
                }
            }
        }
    }

    Scaffold(
        containerColor = Background,
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            // PROFILE HEADER
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "My Profile",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Indigo
                )

                Text(
                    text = "Make your next career move personal.",
                    color = Muted
                )
            }

            // STUDENT INFORMATION
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Lavender
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "YOUR STUDENT PROFILE",
                        color = Blue,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = studentName.ifBlank { "Student" },
                        color = Indigo,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = "$course · $university",
                        color = Muted,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    HorizontalDivider(
                        color = Color(0xFFD8D0F4)
                    )

                    ProfileField(
                        label = "Full name",
                        value = studentName,
                        onValueChange = onStudentNameChange
                    )

                    ProfileField(
                        label = "Course",
                        value = course,
                        onValueChange = { course = it }
                    )

                    ProfileField(
                        label = "University",
                        value = university,
                        onValueChange = { university = it }
                    )
                }
            }

            // SKILLS AND INTERESTS
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Career interests & skills",
                        fontWeight = FontWeight.Bold,
                        color = Indigo,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "Update your interests and skills " +
                                "to prepare for future job matching.",
                        color = Muted,
                        style = MaterialTheme.typography.bodySmall
                    )

                    ProfileField(
                        label = "Career interests (comma-separated)",
                        value = interests,
                        onValueChange = { interests = it }
                    )

                    ProfileField(
                        label = "Skills (comma-separated)",
                        value = skills,
                        onValueChange = { skills = it }
                    )
                }
            }

            // CV MANAGEMENT
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "My CV",
                        fontWeight = FontWeight.Bold,
                        color = Indigo,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = "Choose your CV to prepare it " +
                                "for upload and future AI analysis.",
                        color = Muted
                    )

                    Surface(
                        color = Lavender,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment =
                                Alignment.CenterHorizontally,
                            verticalArrangement =
                                Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.UploadFile,
                                contentDescription = null,
                                tint = Blue,
                                modifier = Modifier.size(36.dp)
                            )

                            Text(
                                text = "PDF, DOC or DOCX · Up to 5 MB",
                                color = Indigo,
                                style = MaterialTheme.typography.bodySmall
                            )

                            Button(
                                onClick = {
                                    picker.launch(
                                        arrayOf(
                                            "application/pdf",
                                            "application/msword",
                                            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                                        )
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Blue
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Choose CV")
                            }
                        }
                    }

                    if (profileViewModel.selectedCvUri != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Description,
                                contentDescription = null,
                                tint = Blue
                            )

                            Spacer(Modifier.width(10.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = profileViewModel.selectedCvName
                                        ?: "Selected CV",
                                    color = Indigo,
                                    fontWeight = FontWeight.Medium
                                )

                                profileViewModel.selectedCvSize?.let { size ->
                                    Text(
                                        text = "${size / 1024} KB",
                                        color = Muted,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }

                            TextButton(
                                onClick = profileViewModel::removeCv,
                                enabled = !profileViewModel.isUploading
                            ) {
                                Text("Remove", color = Blue)
                            }
                        }

                        Button(
                            onClick = {
                                // Temporary student ID until
                                // authentication is implemented.
                                profileViewModel.uploadCv(
                                    studentId = 1
                                )
                            },
                            enabled = !profileViewModel.isUploading,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Blue
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (profileViewModel.isUploading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp,
                                    color = Color.White
                                )
                            } else {
                                Text("Upload CV")
                            }
                        }
                    }

                    profileViewModel.uploadMessage?.let { message ->
                        Text(
                            text = message,
                            color = if (
                                profileViewModel.uploadSucceeded
                            ) {
                                Blue
                            } else {
                                Color(0xFF9B5A30)
                            }
                        )
                    }

                    Text(
                        text = "Backend integration pending. " +
                                "Selecting a file does not upload it " +
                                "or perform AI analysis.",
                        color = Muted,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label)
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Blue,
            unfocusedBorderColor = Color(0xFFD6DDF1),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}
