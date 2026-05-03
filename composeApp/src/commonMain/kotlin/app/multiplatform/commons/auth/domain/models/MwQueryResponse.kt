package app.multiplatform.commons.auth.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class MwQueryResponse(
    val query: Query? = null
) {
    @Serializable
    data class Query(
        val tokens: Tokens? = null,
        val userinfo: UserInfo? = null,
        val users: List<User>? = null
    ) {
        fun loginToken(): String? = tokens?.logintoken
        fun userInfo(): UserInfo? = userinfo
        fun getUserResponse(userName: String): User? = users?.find { it.name == userName }
    }

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
