package com.isfandroid.whattowatch

import android.app.Application
import com.isfandroid.whattowatch.core.di.databaseModule
import com.isfandroid.whattowatch.core.di.networkModule
import com.isfandroid.whattowatch.core.di.repositoryModule
import com.isfandroid.whattowatch.di.useCaseModule
import com.isfandroid.whattowatch.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // Timber
        Timber.plant(Timber.DebugTree())

        // Koin
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@BaseApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}