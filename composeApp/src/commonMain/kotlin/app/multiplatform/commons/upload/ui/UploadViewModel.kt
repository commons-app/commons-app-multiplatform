package app.multiplatform.commons.upload.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.multiplatform.commons.upload.domain.UploadRepository
import app.multiplatform.commons.upload.domain.model.Contribution
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Job
import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.number

class UploadViewModel(
    private val uploadRepository: UploadRepository,
    private val settings: Settings,
) : ViewModel() {

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

            is UploadEvent.OnImageSelected ->
                _uiState.update { it.copy(selectedImageBytes = e.bytes) }

            UploadEvent.OnToggleLicenseMenu ->
                _uiState.update { it.copy(isLicenseMenuOpen = !it.isLicenseMenuOpen) }

            UploadEvent.OnStartUpload -> startUpload()

            UploadEvent.OnCancelUpload -> cancelUpload()

            UploadEvent.OnDismissError ->
                _uiState.update { it.copy(uploadError = null, captionError = null) }

            UploadEvent.OnReset -> {
                uploadJob?.cancel()
                uploadJob = null
                _uiState.value = UploadUiState()
            }
        }
    }

    private fun startUpload() {
        val state = _uiState.value
        val caption = state.caption.trim()

        if (caption.isBlank()) {
            _uiState.update { it.copy(captionError = "Caption is required") }
            return
        }
        val imageBytes = state.selectedImageBytes
        if (imageBytes == null) {
            _uiState.update { it.copy(captionError = "Please select an image first") }
            return
        }

        val username = settings["username", ""]
        _uiState.update { it.copy(phase = UploadPhase.UPLOADING, uploadProgress = 0f, uploadError = null) }

        uploadJob = viewModelScope.launch {
            try {
                val contribution = Contribution(
                    caption = caption,
                    description = state.description,
                    license = state.selectedLicense,
                    username = username,
                )
                // Build a filename that is unique per user + moment so we never
                // accidentally overwrite or revise someone else's file on Commons.
                // Pattern: <caption>_<username>_<yyyyMMddHHmmss>.jpg
                val safeName = caption.take(40)
                    .replace(Regex("[^A-Za-z0-9_\\-]"), "_")
                    .trim('_')
                    .ifBlank { "upload" }
                val safeUser = username.take(20)
                    .replace(Regex("[^A-Za-z0-9_\\-]"), "_")
                    .trim('_')
                    .ifBlank { "user" }
                val now = kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.UTC)
                val timestamp = now.year.toString() +
                        now.month.number.toString().padStart(2, '0') +
                        now.day.toString().padStart(2, '0') +
                    now.hour.toString().padStart(2, '0') +
                    now.minute.toString().padStart(2, '0') +
                    now.second.toString().padStart(2, '0')
                val filename = "${safeName}_${safeUser}_${timestamp}.jpg"

                uploadRepository.uploadFileToCommons(
                    filename = filename,
                    fileBytes = imageBytes,
                    mimeType = "image/jpeg",
                    contribution = contribution,
                    onProgress = { bytesSentTotal, contentLength ->
                        val total = contentLength ?: 0L
                        val progress = if (total > 0L)
                            bytesSentTotal.toFloat() / total.toFloat()
                        else 0f
                        _uiState.update { it.copy(uploadProgress = progress.coerceIn(0f, 1f)) }
                    }
                )
                _uiState.update { it.copy(phase = UploadPhase.COMPLETE, uploadProgress = 1f) }
            } catch (e: Exception) {
                Napier.e("Upload failed", e)
                _uiState.update {
                    it.copy(
                        phase = UploadPhase.DETAILS,
                        uploadError = e.message ?: "Upload failed. Please try again.",
                    )
                }
            }
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
