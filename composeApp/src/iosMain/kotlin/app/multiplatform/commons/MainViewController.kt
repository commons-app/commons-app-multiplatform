package app.multiplatform.commons

import androidx.compose.ui.window.ComposeUIViewController
import app.multiplatform.commons.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) {
    App()
}