package net.blakelee.calllog.models

import io.reactivex.Observable

interface CallLog {
    fun observeCallLog(): Observable<List<CallDetails>>
}

data class CallDetails(
    val phoneNumber: String,
    val date: String,
    val direction: String
)