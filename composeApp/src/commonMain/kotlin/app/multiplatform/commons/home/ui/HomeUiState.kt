package app.multiplatform.commons.home.ui

import app.multiplatform.commons.home.domain.model.ContributionItem
import app.multiplatform.commons.model.DataError

data class HomeUiState(
    val isLoading: Boolean = false,
    val contributions: List<ContributionItem> = emptyList(),
    val error: DataError.NetworkError? = null
)
