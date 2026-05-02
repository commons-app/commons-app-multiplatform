package app.multiplatform.commons.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val BASE_URL = "https://commons.wikimedia.beta.wmflabs.org/w/api.php?format=json&formatversion=2&errorformat=plaintext&"
fun createHttpClient(engine: HttpClientEngine): HttpClient = HttpClient(engine) {
    install(DefaultRequest) {
        url(BASE_URL)
    }
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        })
    }
    install(Logging) {
        level = LogLevel.ALL
    }
}