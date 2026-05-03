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
import app.multiplatform.commons.theme.platformThemeTarget
import app.multiplatform.commons.upload.ui.UploadScreen
import app.multiplatform.commons.upload.ui.UploadViewModel
import io.github.robinpcrd.cupertino.adaptive.AdaptiveTheme
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Serializable
sealed interface Route {
    @Serializable
    data object Auth : Route
    @Serializable
    data object Home : Route
    @Serializable
    data object Upload : Route
}

@Composable
@Preview
fun App() {
    AdaptiveTheme(
        target = platformThemeTarget(),
        material = { WikimediaTheme(content = it) }
    ) {
        Surface {
            val authRepository = koinInject<AuthRepository>()
            val initialRoute = remember { 
                if (authRepository.isLoggedIn()) Route.Home else Route.Auth
            }
            val backstack = remember { mutableStateListOf<Route>(initialRoute) }

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
                    HomeScreen(
                        onNavigateToUpload = { backstack.add(Route.Upload) }
                    )
                }
                Route.Upload -> {
                    val uploadViewModel = koinViewModel<UploadViewModel>()
                    val uiState by uploadViewModel.uiState.collectAsStateWithLifecycle()

                    UploadScreen(
                        uiState = uiState,
                        onEvent = uploadViewModel::onEvent,
                        onNavigateBack = { backstack.removeLastOrNull() }
                    )
                }
            }
        }
    }
}
