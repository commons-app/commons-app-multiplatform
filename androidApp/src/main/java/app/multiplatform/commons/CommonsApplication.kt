package app.multiplatform.commons

import android.app.Application
import app.multiplatform.commons.di.initKoin
import org.koin.android.ext.koin.androidContext

class CommonsApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@CommonsApplication)
        }
    }
}