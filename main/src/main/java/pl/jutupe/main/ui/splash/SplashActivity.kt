package pl.jutupe.main.ui.splash

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.app.ActivityCompat
import org.koin.android.viewmodel.ext.android.viewModel
import pl.jutupe.base.view.BaseActivity
import pl.jutupe.main.R
import pl.jutupe.main.databinding.ActivitySplashBinding
import pl.jutupe.main.ui.main.MainActivity
import timber.log.Timber

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(
    R.layout.activity_splash
) {

    override val viewModel: SplashViewModel by viewModel()

    override fun onInitDataBinding() {
        binding.motionLayout.setTransitionListener(
            object : MotionLayout.TransitionListener {
                override fun onTransitionCompleted(layout: MotionLayout?, p1: Int) {
                    viewModel.onSplashAnimationFinished()
                }

                override fun onTransitionChange(
                    layout: MotionLayout?, p1: Int, p2: Int, p3: Float
                ) = Unit

                override fun onTransitionStarted(
                    layout: MotionLayout?, p1: Int, p2: Int
                ) = Unit

                override fun onTransitionTrigger(
                    layout: MotionLayout?, p1: Int, p2: Boolean, p3: Float
                ) = Unit
            }
        )

        viewModel.events.observe(this, this::onViewEvent)
    }

    override fun onResume() {
        super.onResume()
        binding.motionLayout.apply {
            startLayoutAnimation()

            //retry when user came back from settings
            if (currentState == R.id.end) {
                viewModel.onResumeToSplash()
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
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_STORAGE && grantResults.isNotEmpty()) {
            val permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED

            viewModel.onStoragePermissionResult(permissionGranted)
        } else {
            viewModel.onStoragePermissionResult(false)
        }
    }

    private fun onViewEvent(event: SplashViewEvent) {
        when (event) {
            SplashViewEvent.NavigateToMain -> {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }
            SplashViewEvent.RequestStoragePermission -> {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE_STORAGE
                )
            }
            SplashViewEvent.ShowNoPermissionInformation -> {
                AlertDialog.Builder(this, R.style.KiwiDialog_Large)
                    .setTitle(R.string.label_permission)
                    .setMessage(R.string.label_permission_message)
                    .setPositiveButton(R.string.label_settings) { _, _ -> viewModel.onPermissionSettingsClicked() }
                    .setCancelable(false)
                    .create()
                    .show()
            }
            SplashViewEvent.NavigateToAppSettings -> {
                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", packageName, null)
                    )
                )
            }
        }
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

    companion object {
        const val REQUEST_CODE_STORAGE = 1
    }
}