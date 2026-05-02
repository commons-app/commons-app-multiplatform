package app.multiplatform.commons.auth.ui

sealed interface AuthEvent {
    data class OnUsernameChanged(val username: String) : AuthEvent
    data class OnPasswordChanged(val password: String) : AuthEvent
    data class OnTwoFactorAuthCodeChanged(val code: String): AuthEvent
    data object OnLoginClicked : AuthEvent
}