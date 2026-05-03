package app.multiplatform.commons.upload.ui

sealed interface UploadEvent {
    data class OnCaptionChanged(val caption: String) : UploadEvent
    data class OnDescriptionChanged(val description: String) : UploadEvent
    data class OnLicenseSelected(val license: UploadLicense) : UploadEvent
    data class OnImageSelected(val bytes: ByteArray) : UploadEvent
    data object OnToggleLicenseMenu : UploadEvent
    data object OnStartUpload : UploadEvent
    data object OnCancelUpload : UploadEvent
    data object OnDismissError : UploadEvent
    data object OnReset : UploadEvent
}
