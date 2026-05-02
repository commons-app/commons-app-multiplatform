package app.multiplatform.commons.auth.domain

import app.multiplatform.commons.auth.domain.models.ClientLoginResult
import app.multiplatform.commons.model.DataError
import app.multiplatform.commons.model.Result

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<ClientLoginResult, DataError.NetworkError>
    suspend fun loginWithTwoFactorCode(username: String, password: String, twoFactorCode: String): Result<Unit, DataError.NetworkError>
}
