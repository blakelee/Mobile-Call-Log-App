package net.blakelee.calllog

import io.reactivex.Observable

interface CallLogInterface {
    fun observeCallLog(): Observable<List<CallDetails>>
}

data class CallDetails(
    val phoneNumber: String,
    val date: String,
    val direction: String
)