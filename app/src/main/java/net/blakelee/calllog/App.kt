package net.blakelee.calllog

import android.app.Application
import net.blakelee.calllog.providers.AppModelProvider
import net.blakelee.calllog.providers.Provider

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val modelProvider = AppModelProvider(this)
        Provider.onCreate(this, modelProvider)
    }
}