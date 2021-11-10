package pl.jutupe.main.ui.main

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sembozdemir.permissionskt.askPermissions
import com.sembozdemir.permissionskt.handlePermissionsResult
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel
import pl.jutupe.home.ui.HomeScreen
import pl.jutupe.home.ui.controller.BottomMediaController
import pl.jutupe.home.ui.controller.BottomMediaControllerViewModel
import pl.jutupe.main.R
import pl.jutupe.main.ui.drawer.DrawerScreen
import pl.jutupe.main.ui.drawer.KiwiDrawer
import pl.jutupe.playback.PlaybackScreen
import pl.jutupe.playback.PlaybackViewModel
import pl.jutupe.settings.ui.SettingsScreen
import pl.jutupe.theme.ui.ThemePickerScreen
import pl.jutupe.ui.theme.KiwiTheme
import pl.jutupe.ui.theme.ThemeDataStore
import pl.jutupe.ui.util.BackdropHeader
import timber.log.Timber

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        requestStoragePermissions()
    }

    private fun requestStoragePermissions() {
        val permissions = mutableListOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        askPermissions(*permissions.toTypedArray()) {
            onShowRationale { showRationaleDialog { it.retry() } }
            onGranted {
                setContent {
                    MainContent()
                }
            }
            onDenied { requestStoragePermissions() }
            onNeverAskAgain { showNoPermissionDialog() }
        }
    }

    private fun showRationaleDialog(action: () -> Unit) {
        android.app.AlertDialog.Builder(this)
            .setTitle(R.string.label_permission_rationale)
            .setMessage(R.string.label_permission_rationale_message)
            .setPositiveButton(android.R.string.ok) { _, _ -> action() }
            .setOnCancelListener { action() }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun showNoPermissionDialog() {
        android.app.AlertDialog.Builder(this)
            .setTitle(R.string.label_permission_settings)
            .setMessage(R.string.label_permission_settings_message)
            .setPositiveButton(R.string.label_settings) { _, _ -> openAppSettings() }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun openAppSettings() {
        val settingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )

        startActivity(settingsIntent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        handlePermissionsResult(requestCode, permissions, grantResults)

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}

@Composable
@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterialApi::class,
)
private fun MainContent(
    themeDataStore: ThemeDataStore = get(),
    playbackViewModel: PlaybackViewModel = getViewModel(),
) {
    val screens = remember {
        listOf(
            DrawerScreen.Home,
            DrawerScreen.Settings,
            DrawerScreen.Theme,
        )
    }
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scaffoldState = rememberScaffoldState(
        drawerState = drawerState,
    )
    val currentTheme by themeDataStore.currentThemeFlow.collectAsState(null)
    val systemUiController = rememberSystemUiController()

    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    KiwiTheme(theme = currentTheme ?: KiwiTheme.Dark) {
        val statusBarColor = MaterialTheme.colors.primary

        SideEffect {
            if (currentTheme == null) return@SideEffect

            systemUiController.setStatusBarColor(
                color = statusBarColor,
                darkIcons = false
            )
        }

        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = {
                KiwiDrawer(
                    screens = screens,
                    currentRoute = currentRoute,
                ) {
                    if (it.route != currentRoute) {
                        navController.navigate(it.route)
                    }
                    scope.launch {
                        drawerState.close()
                    }
                }
            },
            drawerShape = RectangleShape,
            drawerBackgroundColor = MaterialTheme.colors.surface,
        ) {
            NavHost(navController, startDestination = DrawerScreen.Home.route,
                modifier = Modifier
                    .background(color = MaterialTheme.colors.primary),
            ) {
                composable(DrawerScreen.Home.route) {
                    HomeScreen(
                        onBack = {
                            scope.launch {
                                drawerState.open()
                            }
                        },
                    )
                }

                composable(DrawerScreen.Settings.route) {
                    SettingsScreen(
                        onBack = {
                            scope.launch {
                                drawerState.open()
                            }
                        },
                    )
                }

                composable(DrawerScreen.Theme.route) {
                    ThemePickerScreen(
                        onBack = {
                            scope.launch {
                                drawerState.open()
                            }
                        },
                    )
                }
            }

            //todo bottom media controller with animations
            PlaybackScreen(onBack = { playbackViewModel.onDownSwiped() })
        }
    }
}