package app.multiplatform.commons.upload.ui

data class UploadUiState(
    val caption: String = "",
    val description: String = "",
    val selectedLicense: UploadLicense = UploadLicense.CC_BY_SA_4,
    val isLicenseMenuOpen: Boolean = false,
    val phase: UploadPhase = UploadPhase.DETAILS,
    val uploadProgress: Float = 0f,
    val captionError: String? = null,
    val uploadError: String? = null,
)

enum class UploadPhase { DETAILS, UPLOADING, COMPLETE }

enum class UploadLicense(val label: String) {
    CC_BY_SA_4("CC BY-SA 4.0"),
    CC_BY_4("CC BY 4.0"),
    CC0("CC0 1.0 (Public Domain)"),
}
