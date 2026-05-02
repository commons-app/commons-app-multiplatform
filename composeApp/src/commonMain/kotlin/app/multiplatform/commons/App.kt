package app.multiplatform.commons

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.multiplatform.commons.auth.ui.AuthScreen
import app.multiplatform.commons.auth.ui.AuthViewModel
import app.multiplatform.commons.home.ui.HomeScreen
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
sealed interface Route {
    @Serializable
    data object Auth : Route
    @Serializable
    data object Home : Route
}

@Composable
@Preview
fun App() {
    MaterialTheme {
        Surface {
            val backstack = remember { mutableStateListOf<Route>(Route.Auth) }

            when (backstack.last()) {
                Route.Auth -> {
                    val authViewModel = koinViewModel<AuthViewModel>()
                    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()

                    AuthScreen(
                        uiState = uiState,
                        onEvent = authViewModel::onEvent,
                        onLoginSuccess = {
                            backstack.add(Route.Home)
                        }
                    )
                }
                Route.Home -> {
                    HomeScreen()
                }
            }
        }
    }
}
