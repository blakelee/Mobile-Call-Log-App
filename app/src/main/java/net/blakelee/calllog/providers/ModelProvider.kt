package net.blakelee.calllog.providers

import net.blakelee.calllog.models.CallLogRepository

interface ModelProvider {
    val callLogRepository: CallLogRepository
}