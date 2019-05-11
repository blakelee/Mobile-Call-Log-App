package net.blakelee.calllog.models

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.provider.CallLog.Calls
import android.telephony.PhoneNumberUtils
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_FORMAT = "h:mm a M/d/yy"

class CallLogRepository(private val context: Context) : CallLog {

    private val sdf = SimpleDateFormat(DATE_FORMAT, Locale.US)

    init {
        sdf.timeZone = TimeZone.getDefault()
    }

    private fun createBroadcastReceiver(callback: () -> Unit) = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            val phoneStateListener = object : PhoneStateListener() {
                override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                    if (state == TelephonyManager.CALL_STATE_IDLE) {
                        callback()
                    }
                }
            }

            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
        }
    }

    @SuppressLint("Recycle") // returns empty list when cursor is null so you don't have to close it
    private fun getCallDetails(): List<CallDetails> {
        val cursor = context.contentResolver.query(Calls.CONTENT_URI, null, null, null,
            Calls.DATE + " DESC LIMIT 50") ?: return emptyList()

        val number = cursor.getColumnIndex(Calls.NUMBER)
        val type = cursor.getColumnIndex(Calls.TYPE)
        val date = cursor.getColumnIndex(Calls.DATE)

        val items = mutableListOf<CallDetails>()

        while(cursor.moveToNext()) {
            val phoneNumber = formatPhoneNumber(cursor.getString(number))
            val callDate = sdf.format(Date(cursor.getLong(date)))
            val direction = when(cursor.getInt(type)) {
                Calls.OUTGOING_TYPE -> "OUTGOING"
                Calls.INCOMING_TYPE -> "INCOMING"
                Calls.MISSED_TYPE -> "MISSED"
                Calls.VOICEMAIL_TYPE -> "VOICEMAIL"
                Calls.REJECTED_TYPE -> "REJECTED"
                Calls.ANSWERED_EXTERNALLY_TYPE -> "ANSWERED EXTERNALLY"
                else -> "UNKNOWN"
            }

            items.add(CallDetails(phoneNumber, callDate, direction))
        }

        cursor.close()
        return items
    }

    private fun formatPhoneNumber(phoneNumber: String): String {
        return PhoneNumberUtils.formatNumberToE164(phoneNumber, Locale.US.country)
            .takeLast(10)
            .replaceFirst("(\\d{3})(\\d{3})(\\d+)".toRegex(), "$1-$2-$3")
    }

    override fun observeCallLog(): Observable<List<CallDetails>> {
        return Observable.create { emitter ->
            val receiver = createBroadcastReceiver {
                emitter.onNext(getCallDetails())
            }

            val filter = IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED).apply {
                addAction(Intent.ACTION_CALL)
            }

            context.registerReceiver(receiver, filter)

            emitter.setDisposable(Disposables.fromAction {
                context.unregisterReceiver(receiver)
            })

            emitter.onNext(getCallDetails())
        }
    }
}