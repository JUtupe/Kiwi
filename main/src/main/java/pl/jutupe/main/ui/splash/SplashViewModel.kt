package pl.jutupe.main.ui.splash

import android.Manifest
import android.app.Application
import androidx.core.content.PermissionChecker
import androidx.lifecycle.AndroidViewModel
import pl.jutupe.base.SingleLiveData

class SplashViewModel(
    application: Application
) : AndroidViewModel(application) {

    val events = SingleLiveData<SplashViewEvent>()

    fun onSplashAnimationFinished() {
        val storagePermission = PermissionChecker.checkSelfPermission(getApplication(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (storagePermission == PermissionChecker.PERMISSION_GRANTED) {
            events.value = SplashViewEvent.NavigateToMain
        } else {
            events.value = SplashViewEvent.RequestStoragePermission
        }
    }

    fun onStoragePermissionResult(permissionGranted: Boolean) {
        if (permissionGranted) {
            events.value = SplashViewEvent.NavigateToMain
        } else {
            events.value = SplashViewEvent.ShowNoPermissionInformation
        }
    }

    fun onPermissionSettingsClicked() {
        events.value = SplashViewEvent.NavigateToAppSettings
    }

    fun onResumeToSplash() {
        val storagePermission = PermissionChecker.checkSelfPermission(getApplication(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (storagePermission == PermissionChecker.PERMISSION_GRANTED) {
            events.value = SplashViewEvent.NavigateToMain
        }
    }
}