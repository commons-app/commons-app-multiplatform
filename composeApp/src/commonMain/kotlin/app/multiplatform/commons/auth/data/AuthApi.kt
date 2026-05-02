package app.multiplatform.commons.auth.data

import app.multiplatform.commons.auth.domain.models.MwQueryResponse
import app.multiplatform.commons.utils.Constants.MW_API_PREFIX
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.parameters

class AuthApi(private val client: HttpClient) {
    suspend fun getLoginToken(): MwQueryResponse {
        return client.get("$MW_API_PREFIX&") {
            parameter("action", "query")
            parameter("meta", "tokens")
            parameter("type", "login")
        }.body()
    }

    suspend fun getCsrfToken(): MwQueryResponse {
        return client.get("$MW_API_PREFIX&") {
            parameter("action", "query")
            parameter("meta", "tokens")
            parameter("type", "csrf")
        }.body()
    }

    suspend fun postLogin(
        username: String,
        password: String,
        token: String,
        userLanguage: String,
        loginReturnUrl: String
    ): HttpResponse {
        return client.submitForm(
            url = "${MW_API_PREFIX}&action=clientlogin&rememberMe=",
            formParameters = parameters {
                append("username", username)
                append("password", password)
                append("logintoken", token)
                append("uselang", userLanguage)
                append("loginreturnurl", loginReturnUrl)
            }
        )
    }

    suspend fun postLogin(
        user: String?,
        pass: String?,
        retypedPass: String?,
        twoFactorCode: String?,
        emailAuthToken: String?,
        loginToken: String?,
        userLanguage: String?,
        loginContinue: Boolean,
    ): HttpResponse {
        return client.submitForm(
            url = "${MW_API_PREFIX}&action=clientlogin&rememberMe=",
            formParameters = parameters {
                user?.let { append("username", it) }
                pass?.let { append("password", it) }
                retypedPass?.let { append("retype", it) }
                twoFactorCode?.let { append("OATHToken", it) }
                emailAuthToken?.let { append("token", it) }
                loginToken?.let { append("logintoken", it) }
                userLanguage?.let { append("uselang", it) }
                append("logincontinue", loginContinue.toString())
            }
        ) {
            header("Cache-Control", "no-cache")
        }.body()
    }

    suspend fun getUserInfo(userName: String): MwQueryResponse {
        return client.get("$MW_API_PREFIX&") {
            parameter("action", "query")
            parameter("meta", "userinfo")
            parameter("list", "users")
            parameter("ususers", userName)
            parameter("usprop", "groups|cancreate")
        }.body()
    }
}
