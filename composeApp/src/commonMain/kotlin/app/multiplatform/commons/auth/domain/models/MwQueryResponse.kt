package app.multiplatform.commons.auth.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MwQueryResponse(
    val query: MwQuery? = null,
    @SerialName("continue")
    private val continuation: Map<String, String>? = null
) {
    @Serializable
    data class MwQuery(
        val tokens: Tokens? = null,
        val userinfo: UserInfo? = null,
        val users: List<User>? = null,
        val pages: List<MwQueryPage>? = null
    ) {
        fun loginToken(): String? = tokens?.logintoken
        fun userInfo(): UserInfo? = userinfo
        fun getUserResponse(userName: String): User? = users?.find { it.name == userName }
    }

    @Serializable
    data class MwQueryPage(
        val pageid: Int = 0,
        val title: String? = null,
        val imageinfo: List<ImageInfo>? = null
    )

    @Serializable
    data class ImageInfo(
        val user: String? = null,
        val thumburl: String? = null,
        val thumbwidth: Int? = null,
        val thumbheight: Int? = null,
        val url: String? = null,
        val descriptionurl: String? = null,
        val descriptionshorturl: String? = null,
        val extmetadata: ExtMetadata? = null
    )

    @Serializable
    data class ExtMetadata(
        val DateTime: MetadataValue? = null,
        val Categories: MetadataValue? = null,
        val GPSLatitude: MetadataValue? = null,
        val GPSLongitude: MetadataValue? = null,
        val ImageDescription: MetadataValue? = null,
        val Artist: MetadataValue? = null,
        val LicenseShortName: MetadataValue? = null,
        val LicenseUrl: MetadataValue? = null
    )

    @Serializable
    data class MetadataValue(
        val value: String? = null,
        val source: String? = null,
        val hidden: String? = null
    )

    @Serializable
    data class Tokens(
        val logintoken: String? = null,
        val csrftoken: String? = null,
    )

    @Serializable
    data class UserInfo(
        val id: Int? = null,
        val name: String? = null
    )

    @Serializable
    data class User(
        val name: String? = null,
        val groups: List<String>? = null
    ) {
        fun getGroups(): Set<String> = groups?.toSet() ?: emptySet()
    }
}
