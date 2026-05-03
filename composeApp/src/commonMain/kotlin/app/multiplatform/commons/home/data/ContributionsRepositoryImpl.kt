package app.multiplatform.commons.home.data

import app.multiplatform.commons.auth.domain.AuthRepository
import app.multiplatform.commons.auth.domain.models.MwQueryResponse
import app.multiplatform.commons.home.domain.ContributionsRepository
import app.multiplatform.commons.home.domain.model.ContributionItem
import app.multiplatform.commons.model.DataError
import app.multiplatform.commons.model.Result
import app.multiplatform.commons.upload.data.MediaApi
import io.github.aakira.napier.Napier

class ContributionsRepositoryImpl(
    private val mediaApi: MediaApi,
    private val authRepository: AuthRepository,
) : ContributionsRepository {
    override suspend fun getContributions(
        itemLimit: Int,
        continuation: Map<String, String>
    ): Result<List<ContributionItem>, DataError.NetworkError> {
        return try {
            val username = authRepository.getUsername()
            if (username != null) {
                val response = mediaApi.getMediaListForUser(username, itemLimit, continuation)
                val items = response.query?.pages?.map { it.toContributionItem() } ?: emptyList()
                Result.Success(items)
            } else {
                Result.Error(DataError.NetworkError.UNAUTHORIZED)
            }
        } catch (e: Exception) {
            Napier.e("Failed to get contributions for user", e)
            Result.Error(DataError.NetworkError.UNKNOWN)
        }
    }

    private fun MwQueryResponse.MwQueryPage.toContributionItem(): ContributionItem {
        val info = imageinfo?.firstOrNull()
        return ContributionItem(
            pageId = pageid,
            title = title ?: "",
            userName = info?.user ?: "",
            imageUrl = info?.thumburl ?: "",
            dateUploaded = info?.extmetadata?.DateTime?.value
        )
    }
}
