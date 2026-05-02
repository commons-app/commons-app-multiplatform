package app.multiplatform.commons.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.multiplatform.commons.theme.platformThemeTarget
import commons.composeapp.generated.resources.Res
import commons.composeapp.generated.resources.commons_logo
import io.github.alexzhirkevich.cupertino.CupertinoTextField
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveButton
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveCircularProgressIndicator
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveTheme
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveWidget
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AuthScreen(
    uiState: AuthUiState,
    onEvent: (AuthEvent) -> Unit,
    onLoginSuccess: () -> Unit
) {
    LaunchedEffect(uiState.isLoggedInSuccess) {
        if (uiState.isLoggedInSuccess) {
            onLoginSuccess()
        }
    }

    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.commons_logo),
            contentDescription = "Wikimedia Commons logo",
            modifier = Modifier
                .padding(bottom = 16.dp)
                .size(80.dp)
        )

        Text(
            text = "Wikimedia Commons",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Text(
            text = uiState.error ?: "",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            AdaptiveOutlinedTextField(
                value = uiState.username,
                onValueChange = { onEvent(AuthEvent.OnUsernameChanged(it)) },
                label = "Username",
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            AdaptiveOutlinedTextField(
                value = uiState.password,
                onValueChange = { onEvent(AuthEvent.OnPasswordChanged(it)) },
                label = "Password",
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    TextButton(
                        onClick = { passwordVisible = !passwordVisible },
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Text(
                            text = if (passwordVisible) "Hide" else "Show",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            )

            if (uiState.shouldShowTwoFactorAuthState) {
                AdaptiveOutlinedTextField(
                    value = uiState.twoFactorAuthCode,
                    onValueChange = { onEvent(AuthEvent.OnTwoFactorAuthCodeChanged(it)) },
                    label = "OTP",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (uiState.isLoading) {
            AdaptiveCircularProgressIndicator()
        } else {
            AdaptiveButton(
                onClick = { onEvent(AuthEvent.OnLoginClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Login")
            }
        }
    }
}

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
private fun AdaptiveOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    AdaptiveWidget(
        material = {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text(label) },
                modifier = modifier,
                singleLine = true,
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                trailingIcon = trailingIcon
            )
        },
        cupertino = {
            CupertinoTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(label) },
                modifier = modifier,
                singleLine = true,
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                trailingIcon = trailingIcon
            )
        }
    )
}

@Preview
@Composable
fun AuthScreenPreview() {
    AdaptiveTheme(
        target = platformThemeTarget(),
        material = { MaterialTheme(content = it) }
    ) {
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
