package app.multiplatform.commons.auth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AuthScreen(
    uiState: AuthUiState,
    onEvent: (AuthEvent) -> Unit,
    onLoginSuccess: () -> Unit
) {
    LaunchedEffect(uiState.isLoggedInSuccess) {
        if(uiState.isLoggedInSuccess) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login to Commons",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Text(
            text = uiState.error ?: "",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 16.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.username,
                onValueChange = { onEvent(AuthEvent.OnUsernameChanged(it)) },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.password,
                onValueChange = { onEvent(AuthEvent.OnPasswordChanged(it)) },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            if(uiState.shouldShowTwoFactorAuthState) {
                OutlinedTextField(
                    value = uiState.twoFactorAuthCode,
                    onValueChange = { onEvent(AuthEvent.OnTwoFactorAuthCodeChanged(it)) },
                    label = { Text("OTP") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { onEvent(AuthEvent.OnLoginClicked) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
        }
    }
}

@Preview
@Composable
fun AuthScreenPreview() {
    MaterialTheme {
        Surface {
            AuthScreen(
                uiState = AuthUiState(
                    username = "sampleUser",
                    password = "password123",
                    twoFactorAuthCode = "",
                    error = "Invalid username or password",
                    shouldShowTwoFactorAuthState = true,
                ),
                onEvent = {},
                onLoginSuccess = {}
            )
        }
    }
}
