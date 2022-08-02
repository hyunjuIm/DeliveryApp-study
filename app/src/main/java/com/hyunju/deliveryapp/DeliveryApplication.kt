package com.hyunju.deliveryapp

import android.app.Application
import android.content.Context
import com.hyunju.deliveryapp.di.appModule
import org.koin.core.context.startKoin

class DeliveryApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this

        startKoin {
            modules(appModule)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        appContext = null
    }

    companion object {
        var appContext: Context? = null
            private set
    }
}