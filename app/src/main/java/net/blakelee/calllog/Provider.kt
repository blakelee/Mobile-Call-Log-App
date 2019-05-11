package net.blakelee.calllog

object Provider {
    private lateinit var modelProvider: ModelProvider

    val callLogViewModel by lazy { CallLogViewModel(modelProvider.callLogRepository) }

    fun onCreate(modelProvider: ModelProvider) {
        this.modelProvider = modelProvider
    }
}