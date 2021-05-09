package pl.jutupe.main.ui.splash

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import com.sembozdemir.permissionskt.askPermissions
import com.sembozdemir.permissionskt.handlePermissionsResult
import org.koin.android.viewmodel.ext.android.viewModel
import pl.jutupe.base.view.BaseActivity
import pl.jutupe.main.R
import pl.jutupe.main.databinding.ActivitySplashBinding
import pl.jutupe.main.ui.main.MainActivity
import pl.jutupe.main.util.TransitionCompletedListener

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(
    R.layout.activity_splash
) {

    private var settingsClicked = false

    override val viewModel: SplashViewModel by viewModel()

    private val transitionCompletedListener = TransitionCompletedListener {
        requestStoragePermissions()
    }

    override fun onInitDataBinding() {
        binding.motionLayout.setTransitionListener(transitionCompletedListener)
    }

    override fun onResume() {
        super.onResume()
        binding.motionLayout.apply {
            //retry when user came back from settings
            if (settingsClicked && currentState == R.id.end) {
                settingsClicked = false

                requestStoragePermissions()
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUIAndNavigation(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) = handlePermissionsResult(requestCode, permissions, grantResults)

    override fun onDestroy() {
        binding.motionLayout.removeTransitionListener(transitionCompletedListener)

        super.onDestroy()
    }

    private fun hideSystemUIAndNavigation(activity: Activity) {
        val decorView: View = activity.window.decorView
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    private fun requestStoragePermissions() {
        val permissions = mutableListOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        askPermissions(*permissions.toTypedArray()) {
            onShowRationale { showRationaleDialog { it.retry() } }
            onGranted { openMainActivity() }
            onDenied { requestStoragePermissions() }
            onNeverAskAgain { showNoPermissionDialog() }
        }
    }

    private fun showRationaleDialog(action: () -> Unit) {
        AlertDialog.Builder(this@SplashActivity, R.style.KiwiDialog_Large)
            .setTitle(R.string.label_permission_rationale)
            .setMessage(R.string.label_permission_rationale_message)
            .setPositiveButton(android.R.string.ok) { _, _ -> action() }
            .setOnCancelListener { action() }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun showNoPermissionDialog() {
        AlertDialog.Builder(this@SplashActivity, R.style.KiwiDialog_Large)
            .setTitle(R.string.label_permission_settings)
            .setMessage(R.string.label_permission_settings_message)
            .setPositiveButton(R.string.label_settings) { _, _ -> openAppSettings() }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun openMainActivity() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }

    private fun openAppSettings() {
        settingsClicked = true

        val settingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )

        startActivity(settingsIntent)
    }
}