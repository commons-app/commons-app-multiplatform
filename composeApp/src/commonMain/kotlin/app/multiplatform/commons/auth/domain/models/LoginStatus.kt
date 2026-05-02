package app.multiplatform.commons.auth.domain.models

import kotlinx.serialization.Serializable

@Serializable
enum class LoginStatus {
    PASS,
    FAIL,
    UI,
    REDIRECT,
    RESTART,
    UNKNOWN,
}