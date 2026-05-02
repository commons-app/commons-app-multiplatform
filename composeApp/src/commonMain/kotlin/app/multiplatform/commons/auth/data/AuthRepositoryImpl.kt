package app.multiplatform.commons.auth.data

import app.multiplatform.commons.auth.data.dto.LoginResponseDto
import app.multiplatform.commons.auth.domain.AuthRepository
import app.multiplatform.commons.auth.domain.models.ClientLoginResult
import app.multiplatform.commons.model.DataError
import app.multiplatform.commons.model.Result
import app.multiplatform.commons.utils.Constants
import io.ktor.client.call.body

class AuthRepositoryImpl(
    private val authApi: AuthApi
) : AuthRepository {
    override suspend fun login(username: String, password: String): Result<ClientLoginResult, DataError.NetworkError> {
        return try {
            val tokenResponse = authApi.getLoginToken()
            val token = tokenResponse.query?.loginToken() 
                ?: return Result.Error(DataError.NetworkError.SERVER_ERROR)

            val loginResponse = authApi.postLogin(
                username = username,
                password = password,
                token = token,
                userLanguage = "en", // TODO("Replace with user's preferred locale")
                loginReturnUrl = Constants.WIKIPEDIA_URL
            )

            return when(loginResponse.status.value) {
                in 200..299 -> {
                    val responseBody = loginResponse.body<LoginResponseDto>()
                    Result.Success(ClientLoginResult(responseBody.clientlogin?.status, responseBody.clientlogin?.message))
                }
                400 -> Result.Error(DataError.NetworkError.BAD_REQUEST)
                409 -> Result.Error(DataError.NetworkError.CONFLICT)
                429 -> Result.Error(DataError.NetworkError.TOO_MANY_REQUESTS)
                in 500..599 -> Result.Error(DataError.NetworkError.SERVER_ERROR)
                else -> Result.Error(DataError.NetworkError.UNKNOWN)
            }
        } catch (e: Exception) {
            Result.Error(DataError.NetworkError.UNKNOWN)
        }
    }

    override suspend fun loginWithTwoFactorCode(
        username: String,
        password: String,
        twoFactorCode: String
    ): Result<Unit, DataError.NetworkError> {
        TODO("Not yet implemented")
    }
}
