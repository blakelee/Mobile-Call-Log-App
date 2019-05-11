package net.blakelee.calllog.providers

import android.content.Context
import net.blakelee.calllog.models.CallLogRepository

class AppModelProvider(context: Context) : ModelProvider {

    override val callLogRepository: CallLogRepository =
        CallLogRepository(context)
}