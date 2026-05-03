package app.multiplatform.commons.upload.data

import app.multiplatform.commons.auth.domain.AuthRepository
import app.multiplatform.commons.upload.domain.UploadRepository
import app.multiplatform.commons.upload.domain.model.Contribution
import app.multiplatform.commons.upload.domain.model.StashUploadResult
import app.multiplatform.commons.utils.Constants
import io.github.aakira.napier.Napier
import io.ktor.client.content.ProgressListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UploadRepositoryImpl(
    private val uploadApi: UploadApi,
    private val authRepository: AuthRepository,
    private val pageContentsCreator: PageContentsCreator,
): UploadRepository {
    private val chunkSize = 512 * 1024 // 512 KB

    /**
     * This is maximum duration for which a stash is persisted on MediaWiki
     * https://www.mediawiki.org/wiki/Manual:$wgUploadStashMaxAge
    */
    private val maxChunkAge = 6 * 3600 * 1000 // 6 hours

    override fun uploadFileToStash(filename: String, ): Flow<StashUploadResult> = flow {
        TODO("Not yet implemented")
    }

    override suspend fun uploadFileFromStash(
        token: String,
        text: String,
        comment: String,
        filename: String,
        fileKey: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun uploadFileToCommons(
        filename: String,
        fileBytes: ByteArray,
        mimeType: String?,
        contribution: Contribution,
        onProgress: ProgressListener
    ) {
        val csrfToken = authRepository.getCsrfToken()
            ?: throw IllegalStateException("Could not fetch CSRF token")

        try {
            uploadApi.uploadFileToCommons(
                filename = filename,
                fileBytes = fileBytes,
                mimeType = mimeType,
                token = csrfToken,
                text = pageContentsCreator.createFrom(contribution),
                comment = Constants.DEFAULT_EDIT_SUMMARY,
                onProgress = onProgress
            )
        } catch (e: Exception) {
            Napier.e("Failed to upload file to Commons", e)
            throw e
        }
    }
}