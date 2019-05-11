package net.blakelee.calllog.viewmodels

import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import net.blakelee.calllog.App
import net.blakelee.calllog.models.CallLogRepository

class CallLogViewModel(app: App,
                       private val repository: CallLogRepository
) : AndroidViewModel(app) {

    val permissionsGranted = BehaviorRelay.create<Boolean>()

    fun checkPermissions(permissions: Array<out String>) {

        if(android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
            val hasPermissions = permissions.all {
                getApplication<App>().checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
            }

            permissionsGranted.accept(hasPermissions)
        } else {
            permissionsGranted.accept(true)
        }
    }

    fun observeCallLog() = repository.observeCallLog()
}