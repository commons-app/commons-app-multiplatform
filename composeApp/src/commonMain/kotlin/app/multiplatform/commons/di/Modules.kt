package app.multiplatform.commons.di

import app.multiplatform.commons.auth.data.AuthApi
import app.multiplatform.commons.auth.data.AuthRepositoryImpl
import app.multiplatform.commons.auth.domain.AuthRepository
import app.multiplatform.commons.auth.ui.AuthViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single { AuthApi(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    viewModel { AuthViewModel(get()) }
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(sharedModule, platformModule)
    }
}