package net.blakelee.calllog

import androidx.lifecycle.ViewModel

class CallLogViewModel(private val repository: CallLogRepository) : ViewModel() {

    fun observeCallLog() = repository.observeCallLog()
}