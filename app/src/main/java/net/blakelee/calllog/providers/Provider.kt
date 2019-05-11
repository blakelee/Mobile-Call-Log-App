package net.blakelee.calllog.providers

import net.blakelee.calllog.App
import net.blakelee.calllog.viewmodels.CallLogViewModel

object Provider {
    private lateinit var app: App
    private lateinit var modelProvider: ModelProvider

    val callLogViewModel by lazy { CallLogViewModel(app, modelProvider.callLogRepository) }

    fun onCreate(app: App, modelProvider: ModelProvider) {
        Provider.app = app
        Provider.modelProvider = modelProvider
    }
}