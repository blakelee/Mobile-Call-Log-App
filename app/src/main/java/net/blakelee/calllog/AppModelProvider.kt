package net.blakelee.calllog

import android.content.Context

class AppModelProvider(context: Context) : ModelProvider {

    override val callLogRepository: CallLogRepository = CallLogRepository(context)
}