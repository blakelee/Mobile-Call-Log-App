package net.blakelee.calllog

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val modelProvider = AppModelProvider(this)
        Provider.onCreate(modelProvider)
    }
}