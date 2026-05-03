package app.multiplatform.commons.di

import app.multiplatform.commons.network.createHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module

actual val platformModule = module {
    single<HttpClient> { createHttpClient(OkHttp.create(), get()) }
}