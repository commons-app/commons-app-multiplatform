package app.multiplatform.commons.upload.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UploadResponseDto(
    val upload: UploadData? = null
) {
    @Serializable
    data class UploadData(
        val result: String? = null,
        val filekey: String? = null,
        val offset: Int? = null,
        val filename: String? = null,
        val sessionkey: String? = null,
        val message: String? = null
    ) {
        fun isSuccessful(): Boolean = result == "Success"

        fun createCanonicalFileName() = "File:$filename"
    }
}
