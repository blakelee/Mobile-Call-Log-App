package net.blakelee.calllog

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import android.net.Uri.fromParts
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.content.Intent

private const val PERMISSIONS = 100
private const val REQUEST_PERMISSION_SETTING = 101

class CallLogActivity : AppCompatActivity() {

    private val permissions = arrayOf(Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_STATE)

    private lateinit var viewModel: CallLogViewModel
    private lateinit var adapter: CallLogAdapter
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = CallLogAdapter()
        recycler.adapter = adapter

        viewModel = Provider.callLogViewModel

        permissionsButton.setOnClickListener { requestPermissions(false) }

        requestPermissions(true)
    }

    override fun onResume() {
        super.onResume()

        if (hasPermissions(this, *permissions)) {
            observeCallLog()
            permissionsButton.visibility = View.GONE
        } else {
            permissionsButton.visibility = View.VISIBLE
        }
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    private fun requestPermissions(isStart: Boolean) {

        if(android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
            val denied = permissions.filter {
                checkSelfPermission(it) == PackageManager.PERMISSION_DENIED
            }

            val anyPermissionIsPermanentlyDenied = denied.any { !shouldShowRequestPermissionRationale(it) }

            if (anyPermissionIsPermanentlyDenied && !isStart) {
                // Open the settings so the user can enable those permissions
                openAppSettings()
            } else {
                // Show standard dialog
                ActivityCompat.requestPermissions(this, permissions, PERMISSIONS)
            }
        }
    }

    private fun observeCallLog() {
        disposable?.dispose()
        disposable = viewModel.observeCallLog().subscribe {
            adapter.setItems(it)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            permissionsButton.visibility = View.GONE
            observeCallLog()
        } else {
            permissionsButton.visibility = View.VISIBLE
        }
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun openAppSettings() {
        val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = fromParts("package", packageName, null)
        startActivityForResult(intent, REQUEST_PERMISSION_SETTING)
    }
}
