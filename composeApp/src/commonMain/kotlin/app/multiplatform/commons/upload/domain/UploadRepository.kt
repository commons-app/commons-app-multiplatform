package app.multiplatform.commons.upload.domain

import app.multiplatform.commons.upload.domain.model.Contribution
import app.multiplatform.commons.upload.domain.model.StashUploadResult
import io.ktor.client.content.ProgressListener
import kotlinx.coroutines.flow.Flow

interface UploadRepository {
    fun uploadFileToStash(
        filename: String,
    ): Flow<StashUploadResult>

    suspend fun uploadFileFromStash(
        token: String,
        text: String,
        comment: String,
        filename: String,
        fileKey: String,
    )

    suspend fun uploadFileToCommons(
        filename: String,
        fileBytes: ByteArray,
        mimeType: String?,
        contribution: Contribution,
        onProgress: ProgressListener
    )

}
