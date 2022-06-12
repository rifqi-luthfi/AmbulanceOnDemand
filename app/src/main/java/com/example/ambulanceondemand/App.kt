package com.example.ambulanceondemand

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.ambulanceondemand.di.dispatcherModule
import com.example.ambulanceondemand.di.networkModule
import com.example.ambulanceondemand.di.repositoryModule
import com.example.ambulanceondemand.di.viewModelModule
import org.checkerframework.checker.formatter.FormatUtil
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@App)
            modules(listOf(networkModule, repositoryModule, viewModelModule, dispatcherModule))
        }

    }
}
