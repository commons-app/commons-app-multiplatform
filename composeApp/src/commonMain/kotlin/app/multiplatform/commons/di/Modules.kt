package app.multiplatform.commons.di

import app.multiplatform.commons.auth.data.AuthApi
import app.multiplatform.commons.auth.data.AuthRepositoryImpl
import app.multiplatform.commons.auth.domain.AuthRepository
import app.multiplatform.commons.auth.ui.AuthViewModel
import app.multiplatform.commons.upload.data.PageContentsCreator
import app.multiplatform.commons.upload.data.UploadApi
import app.multiplatform.commons.upload.ui.UploadViewModel
import app.multiplatform.commons.upload.data.UploadRepositoryImpl
import app.multiplatform.commons.upload.domain.UploadRepository
import com.russhwolf.settings.Settings
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single { Settings() }
    single { AuthApi(get()) }
    single { UploadApi(get()) }
    single { PageContentsCreator() }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<UploadRepository> { UploadRepositoryImpl(get(), get(), get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { UploadViewModel() }
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(sharedModule, platformModule)
    }
}