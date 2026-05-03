package app.multiplatform.commons.home.domain

import app.multiplatform.commons.home.domain.model.ContributionItem
import app.multiplatform.commons.model.DataError
import app.multiplatform.commons.model.Result

interface ContributionsRepository {
    suspend fun getContributions(
        itemLimit: Int,
        continuation: Map<String, String>
    ): Result<List<ContributionItem>, DataError.NetworkError>
}
