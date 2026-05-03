package app.multiplatform.commons.network

import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val BASE_URL = "https://commons.wikimedia.beta.wmcloud.org/"
fun createHttpClient(engine: HttpClientEngine, settings: Settings): HttpClient = HttpClient(engine) {
    install(DefaultRequest) {
        url(BASE_URL)
    }
    install(HttpCookies) {
        storage = SettingsCookieStorage(settings)
    }
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        })
    }
    install(Logging) {
        level = LogLevel.ALL
        logger = object : Logger {
            override fun log(message: String) {
                println("KtorLogger: $message")
            }
        }
    }
}
