package app.multiplatform.commons.upload.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UploadResponseDto(
    val upload: UploadData? = null,
    val errors: List<ApiError>? = null,
) {
    /** Returns the first error message, or null if there are no errors. */
    fun firstError(): String? = errors
        ?.firstOrNull()
        ?.let { "[${it.code}] ${it.text}" }

    @Serializable
    data class UploadData(
        val result: String? = null,
        val filekey: String? = null,
        val offset: Int? = null,
        val filename: String? = null,
        val sessionkey: String? = null,
        val message: String? = null,
        /** Non-null when the server detected a duplicate even with ignorewarnings=1. */
        val warnings: Warnings? = null,
    ) {
        fun isSuccessful(): Boolean = result == "Success"

        fun createCanonicalFileName() = "File:$filename"

        @Serializable
        data class Warnings(
            /** List of existing filenames whose content matches the upload. */
            val duplicate: List<String>? = null,
        )
    }

    @Serializable
    data class ApiError(
        val code: String? = null,
        val text: String? = null,
    )
}
