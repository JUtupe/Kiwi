package pl.jutupe.main.ui.splash

sealed class SplashViewEvent {
    object NavigateToMain : SplashViewEvent()
    object NavigateToAppSettings : SplashViewEvent()

    object RequestStoragePermission: SplashViewEvent()

    object ShowNoPermissionInformation: SplashViewEvent()
}