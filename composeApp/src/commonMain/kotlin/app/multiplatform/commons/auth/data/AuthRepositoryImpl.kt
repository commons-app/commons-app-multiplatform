package app.multiplatform.commons.auth.data

import app.multiplatform.commons.auth.data.dto.LoginResponseDto
import app.multiplatform.commons.auth.domain.AuthRepository
import app.multiplatform.commons.auth.domain.models.ClientLoginResult
import app.multiplatform.commons.auth.domain.models.TwoFactorType
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
                userLanguage = "en",
                loginReturnUrl = Constants.WIKIPEDIA_URL
            )

            return when(loginResponse.status.value) {
                in 200..299 -> {
                    val responseBody = loginResponse.body<LoginResponseDto>()
                    val twoFactorType = responseBody.clientlogin?.requests
                        ?.firstNotNullOfOrNull { req ->
                            when {
                                req.id?.endsWith("TOTPAuthenticationRequest") == true -> TwoFactorType.TOTP
                                req.id?.endsWith("EmailAuthAuthenticationRequest") == true -> TwoFactorType.EMAIL
                                else -> null
                            }
                        }
                    Result.Success(
                        ClientLoginResult(
                            status = responseBody.clientlogin?.status,
                            message = responseBody.clientlogin?.message,
                            twoFactorType = twoFactorType,
                        )
                    )
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
        twoFactorCode: String,
        twoFactorType: TwoFactorType,
    ): Result<ClientLoginResult, DataError.NetworkError> {
        return try {
            // Fetch a fresh loginToken; same session cookie keeps the auth state alive.
            val tokenResponse = authApi.getLoginToken()
            val freshToken = tokenResponse.query?.loginToken()
                ?: return Result.Error(DataError.NetworkError.SERVER_ERROR)

            val loginResponse = authApi.postLogin(
                user = username,
                pass = password,
                retypedPass = null,
                // TOTP (e.g. Google Authenticator) uses OATHToken; email-based auth uses token.
                twoFactorCode = if (twoFactorType == TwoFactorType.TOTP) twoFactorCode else null,
                emailAuthToken = twoFactorCode,
                loginToken = freshToken,
                userLanguage = "en",
                loginContinue = true
            )

            return when (loginResponse.status.value) {
                in 200..299 -> {
                    val responseBody = loginResponse.body<LoginResponseDto>()
                    Result.Success(
                        ClientLoginResult(
                            responseBody.clientlogin?.status,
                            responseBody.clientlogin?.message
                        )
                    )
                }
                400 -> Result.Error(DataError.NetworkError.BAD_REQUEST)
                401 -> Result.Error(DataError.NetworkError.UNAUTHORIZED)
                409 -> Result.Error(DataError.NetworkError.CONFLICT)
                429 -> Result.Error(DataError.NetworkError.TOO_MANY_REQUESTS)
                in 500..599 -> Result.Error(DataError.NetworkError.SERVER_ERROR)
                else -> Result.Error(DataError.NetworkError.UNKNOWN)
            }
        } catch (e: Exception) {
            Result.Error(DataError.NetworkError.UNKNOWN)
        }
    }
}
