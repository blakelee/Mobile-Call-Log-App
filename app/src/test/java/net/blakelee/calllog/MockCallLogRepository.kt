package net.blakelee.calllog

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import net.blakelee.calllog.models.CallDetails
import net.blakelee.calllog.models.CallLog
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class MockCallLogRepository : CallLog {

    override fun observeCallLog(): Observable<List<CallDetails>> {
        return Observable.create { emitter ->
            var cur = 0
            val max = 5

            fun startTimer() {
                Single.timer(3, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                    .subscribe { _ ->
                        emitter.onNext(generateItems())
                        if (++cur < max) {
                            startTimer()
                        }
                    }
            }

            startTimer()
        }
    }

    private fun generateItems(): List<CallDetails> {
        val rand = Random(2)
        return (0 until 50).map {
            CallDetails(
                rand.nextLong(100_000_0000, 999_999_9999).toString(),
                Date().toString(),
                listOf("INCOMING", "OUTGOING").random()
            )
        }
    }
}