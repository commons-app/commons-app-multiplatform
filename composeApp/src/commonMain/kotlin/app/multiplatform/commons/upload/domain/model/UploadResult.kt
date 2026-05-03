package app.multiplatform.commons.upload.domain.model

data class UploadResult(
    val result: String,
    val filekey: String,
    val offset: Int,
    val filename: String,
) {
    fun isSuccessful(): Boolean = result == "Success"
}
