package net.blakelee.calllog

import io.reactivex.Observable
import java.util.*

interface CallLogInterface {
    fun observeCallLog(): Observable<List<CallDetails>>
}

data class CallDetails(
    val phoneNumber: String,
    val date: Date,
    val type: String
)