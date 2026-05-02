package app.multiplatform.commons.auth.ui

data class AuthUiState(
    val isLoading: Boolean = false,
    val username: String = "",
    val password: String = "",
    val twoFactorAuthCode : String = "",
    val error: String? = null,
    val shouldShowTwoFactorAuthState: Boolean = false,
    val isLoggedInSuccess: Boolean = false,
)
