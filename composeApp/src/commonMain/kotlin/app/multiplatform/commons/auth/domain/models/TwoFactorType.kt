package app.multiplatform.commons.auth.domain.models

enum class TwoFactorType {
    /** TOTP (e.g. Google Authenticator) which is sent as `OATHToken` */
    TOTP,

    /** Email-based auth code which is sent as `token` */
    EMAIL
}

