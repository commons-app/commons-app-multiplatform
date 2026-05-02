package app.multiplatform.commons.auth.data.dto

import app.multiplatform.commons.auth.domain.models.LoginStatus
import app.multiplatform.commons.auth.utils.LoginStatusSerializer
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    val clientlogin: ClientLogin? = null
) {
    @Serializable
    data class ClientLogin(
        @Serializable(with = LoginStatusSerializer::class)
        val status: LoginStatus? = null,
        val requests: List<Request>? = null,
        val message: String? = null,
        val username: String? = null
    )

    @Serializable
    data class Request(
        val id: String? = null
    )
}