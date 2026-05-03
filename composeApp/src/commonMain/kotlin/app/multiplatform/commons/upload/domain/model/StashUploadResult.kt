package app.multiplatform.commons.upload.domain.model

data class StashUploadResult(
    val state: StashUploadState,
    val fileKey: String?,
    val errorMessage: String?,
)

enum class StashUploadState {
    SUCCESS,
    PAUSED,
    FAILED,
    CANCELLED,
}