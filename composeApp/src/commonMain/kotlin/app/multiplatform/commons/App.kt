package app.multiplatform.commons

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.multiplatform.commons.auth.domain.AuthRepository
import app.multiplatform.commons.auth.ui.AuthScreen
import app.multiplatform.commons.auth.ui.AuthViewModel
import app.multiplatform.commons.home.ui.HomeScreen
import app.multiplatform.commons.theme.WikimediaTheme
import app.multiplatform.commons.upload.ui.UploadEvent
import app.multiplatform.commons.upload.ui.UploadScreen
import app.multiplatform.commons.upload.ui.UploadViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Serializable
sealed interface Route {
    @Serializable data object Auth : Route
    @Serializable data object Home : Route
    @Serializable data object Upload : Route
}

@Composable
@Preview
fun App() {
    WikimediaTheme {
        Surface {
            val authRepository = koinInject<AuthRepository>()
            val initialRoute = remember {
                if (authRepository.isLoggedIn()) Route.Home else Route.Auth
            }
            val backstack = remember { mutableStateListOf<Route>(initialRoute) }

            val uploadViewModel = koinViewModel<UploadViewModel>()
            val uploadUiState by uploadViewModel.uiState.collectAsStateWithLifecycle()

            when (backstack.last()) {
                Route.Auth -> {
                    val authViewModel = koinViewModel<AuthViewModel>()
                    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()
                    AuthScreen(
                        uiState = uiState,
                        onEvent = authViewModel::onEvent,
                        onLoginSuccess = { backstack.add(Route.Home) }
                    )
                }
                Route.Home -> {
                    HomeScreen(
                        onNavigateToUpload = { imageBytes ->
                            uploadViewModel.onEvent(UploadEvent.OnReset)
                            uploadViewModel.onEvent(UploadEvent.OnImageSelected(imageBytes))
                            backstack.add(Route.Upload)
                        }
                    )
                }
                Route.Upload -> {
                    UploadScreen(
                        uiState = uploadUiState,
                        onEvent = uploadViewModel::onEvent,
                        onNavigateBack = {
                            // Always reset so the next upload starts fresh.
                            uploadViewModel.onEvent(UploadEvent.OnReset)
                            backstack.removeLastOrNull()
                        }
                    )
                }
            }
        }
    }
}
