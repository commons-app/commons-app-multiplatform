package app.multiplatform.commons.auth.domain.models

data class ClientLoginResult(
    val status: LoginStatus?,
    val message: String?,
    val twoFactorType: TwoFactorType? = null,
)
