package app.multiplatform.commons.auth.ui

import app.multiplatform.commons.auth.domain.models.TwoFactorType

data class AuthUiState(
    val isLoading: Boolean = false,
    val username: String = "",
    val password: String = "",
    val twoFactorAuthCode : String = "",
    val error: String? = null,
    val shouldShowTwoFactorAuthState: Boolean = false,
    val twoFactorType: TwoFactorType? = null,
    val isLoggedInSuccess: Boolean = false,
)
