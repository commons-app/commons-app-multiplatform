package app.multiplatform.commons.upload.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UploadViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UploadUiState())
    val uiState = _uiState.asStateFlow()

    private var uploadJob: Job? = null

    fun onEvent(e: UploadEvent) {
        when (e) {
            is UploadEvent.OnCaptionChanged ->
                _uiState.update { it.copy(caption = e.caption, captionError = null) }

            is UploadEvent.OnDescriptionChanged ->
                _uiState.update { it.copy(description = e.description) }

            is UploadEvent.OnLicenseSelected ->
                _uiState.update { it.copy(selectedLicense = e.license, isLicenseMenuOpen = false) }

            UploadEvent.OnToggleLicenseMenu ->
                _uiState.update { it.copy(isLicenseMenuOpen = !it.isLicenseMenuOpen) }

            UploadEvent.OnStartUpload -> startUpload()

            UploadEvent.OnCancelUpload -> cancelUpload()

            UploadEvent.OnDismissError ->
                _uiState.update { it.copy(uploadError = null, captionError = null) }
        }
    }

    private fun startUpload() {
        val caption = _uiState.value.caption.trim()
        if (caption.isBlank()) {
            _uiState.update { it.copy(captionError = "Caption is required") }
            return
        }

        _uiState.update { it.copy(phase = UploadPhase.UPLOADING, uploadProgress = 0f, uploadError = null) }

        uploadJob = viewModelScope.launch {
            // TODO: Simulated chunked upload progress, add actual job here
            val steps = 20
            repeat(steps) { step ->
                delay(200L)
                val progress = (step + 1).toFloat() / steps
                _uiState.update { it.copy(uploadProgress = progress) }
            }
            _uiState.update { it.copy(phase = UploadPhase.COMPLETE, uploadProgress = 1f) }
        }
    }

    private fun cancelUpload() {
        uploadJob?.cancel()
        uploadJob = null
        _uiState.update {
            it.copy(
                phase = UploadPhase.DETAILS,
                uploadProgress = 0f,
                uploadError = null,
            )
        }
    }
}
