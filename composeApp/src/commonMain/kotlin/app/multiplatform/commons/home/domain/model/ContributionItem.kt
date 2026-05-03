package app.multiplatform.commons.home.domain.model

data class ContributionItem(
    val pageId: Int,
    val title: String,
    val userName: String,
    val imageUrl: String,
    val dateUploaded: String?
)
