
package com.example.mobileapplicationproject.viewmodel

import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapplicationproject.data.repository.CvRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val cvRepository: CvRepository? = null
) : ViewModel() {

    var selectedCvUri by mutableStateOf<Uri?>(null)
        private set

    var selectedCvName by mutableStateOf<String?>(null)
        private set

    var selectedCvSize by mutableStateOf<Long?>(null)
        private set

    var isUploading by mutableStateOf(false)
        private set

    var uploadMessage by mutableStateOf<String?>(null)
        private set

    var uploadSucceeded by mutableStateOf(false)
        private set

    fun selectCv(
        uri: Uri,
        name: String,
        size: Long?
    ) {
        selectedCvUri = uri
        selectedCvName = name
        selectedCvSize = size
        uploadMessage = null
        uploadSucceeded = false
    }

    fun removeCv() {
        selectedCvUri = null
        selectedCvName = null
        selectedCvSize = null
        uploadMessage = null
        uploadSucceeded = false
    }

    fun uploadCv(studentId: Int) {
        val uri = selectedCvUri ?: return

        if (cvRepository == null) {
            uploadMessage =
                "CV upload is awaiting backend integration."
            return
        }

        viewModelScope.launch {
            isUploading = true
            uploadMessage = null
            uploadSucceeded = false

            try {
                val response = cvRepository.uploadCv(
                    studentId = studentId,
                    uri = uri
                )

                uploadSucceeded = response.status == "uploaded"

                uploadMessage = response.message
                    ?: if (uploadSucceeded) {
                        "CV uploaded successfully."
                    } else {
                        "CV received. Status: ${response.status}"
                    }

            } catch (exception: CancellationException) {
                throw exception

            } catch (exception: Exception) {
                uploadMessage =
                    "CV upload failed. Please try again."

            } finally {
                isUploading = false
            }
        }
    }
}
