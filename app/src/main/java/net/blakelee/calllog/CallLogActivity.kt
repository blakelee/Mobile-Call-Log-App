package net.blakelee.calllog

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*

private const val PERMISSIONS = 100

class CallLogActivity : AppCompatActivity() {

    private lateinit var viewModel: CallLogViewModel
    private lateinit var adapter: CallLogAdapter
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = CallLogAdapter()
        recycler.adapter = adapter

        viewModel = Provider.callLogViewModel
    }

    override fun onResume() {
        super.onResume()

        disposable = viewModel.observeCallLog().subscribe {
            adapter.setItems(it)
        }
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    private fun checkPermissions() {
        val permissions = arrayOf(Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_STATE)

        if (!hasPermissions(this, *permissions)) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSIONS)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS) {

        }

    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}
