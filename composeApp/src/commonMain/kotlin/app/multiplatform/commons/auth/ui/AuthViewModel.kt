package app.multiplatform.commons.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.multiplatform.commons.auth.domain.AuthRepository
import app.multiplatform.commons.auth.domain.models.LoginStatus
import app.multiplatform.commons.model.DataError.*
import app.multiplatform.commons.model.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(e: AuthEvent) {
        when (e) {
            is AuthEvent.OnUsernameChanged -> {
                _uiState.update { it.copy(username = e.username) }
            }
            is AuthEvent.OnPasswordChanged -> {
                _uiState.update { it.copy(password = e.password) }
            }
            is AuthEvent.OnTwoFactorAuthCodeChanged -> {
                _uiState.update { it.copy(twoFactorAuthCode = e.code) }
            }
            AuthEvent.OnLoginClicked -> {
                login(_uiState.value.username, _uiState.value.password)
            }
        }
    }

    private fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(error = "Username and password cannot be empty") }
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = if (uiState.value.twoFactorAuthCode.isNotBlank()) {
                authRepository.loginWithTwoFactorCode(username, password, uiState.value.twoFactorAuthCode)
            } else {
                authRepository.login(username, password)
            }
            when (result) {
                is Result.Success -> {
                    when (result.data.status) {
                        LoginStatus.PASS -> {
                            _uiState.update { it.copy(isLoading = false, isLoggedInSuccess = true) }
                        }
                        LoginStatus.FAIL -> {
                            _uiState.update { it.copy(isLoading = false, error = result.data.message) }
                        }
                        LoginStatus.UI -> {
                            _uiState.update { it.copy(isLoading = false, shouldShowTwoFactorAuthState = true) }
                        }
                        LoginStatus.RESTART -> {
                            _uiState.update { it.copy(isLoading = false, error = result.data.message) }
                        }
                        else -> {
                            _uiState.update { it.copy(isLoading = false, error = "An unexpected error occurred") }
                        }
                    }
                }
                is Result.Error -> when (result.error) {
                    NetworkError.REQUEST_TIMEOUT -> _uiState.update { it.copy(isLoading = false, error = "Request timed out") }
                    NetworkError.UNAUTHORIZED -> _uiState.update { it.copy(isLoading = false, error = "Invalid credentials") }
                    NetworkError.CONFLICT -> _uiState.update { it.copy(isLoading = false, error = "Account conflict") }
                    NetworkError.BAD_REQUEST -> _uiState.update { it.copy(isLoading = false, error = "Invalid request") }
                    NetworkError.TOO_MANY_REQUESTS -> _uiState.update { it.copy(isLoading = false, error = "Too many attempts") }
                    NetworkError.NO_INTERNET -> _uiState.update { it.copy(isLoading = false, error = "No internet connection") }
                    NetworkError.SERVER_ERROR -> _uiState.update { it.copy(isLoading = false, error = "Server error") }
                    NetworkError.SERIALIZATION -> _uiState.update { it.copy(isLoading = false, error = "Data error") }
                    NetworkError.UNKNOWN -> _uiState.update { it.copy(isLoading = false, error = "An unknown error occurred") }
                }
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
