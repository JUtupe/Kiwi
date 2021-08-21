package pl.jutupe.main.ui.main

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.RectangleShape
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sembozdemir.permissionskt.askPermissions
import com.sembozdemir.permissionskt.handlePermissionsResult
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.jutupe.home.ui.HomePage
import pl.jutupe.home.ui.HomeScreen
import pl.jutupe.home.ui.controller.BottomMediaControllerViewModel
import pl.jutupe.home.ui.library.LibraryViewModel
import pl.jutupe.home.ui.main.MainViewModel
import pl.jutupe.home.ui.search.SearchViewModel
import pl.jutupe.main.R
import pl.jutupe.main.ui.drawer.DrawerScreen
import pl.jutupe.main.ui.drawer.KiwiDrawer
import pl.jutupe.settings.ui.SettingsScreen
import pl.jutupe.theme.ui.ThemePickerScreen
import pl.jutupe.theme.ui.ThemePickerViewModel
import pl.jutupe.ui.theme.KiwiTheme
import pl.jutupe.ui.theme.ThemeDataStore

class MainActivity : ComponentActivity() {

    private val themeDataStore by inject<ThemeDataStore>()

    private val controllerViewModel by viewModel<BottomMediaControllerViewModel>()

    // todo remove after koin fix
    private val mainViewModel by viewModel<MainViewModel>()
    private val libraryViewModel by viewModel<LibraryViewModel>()
    private val searchViewModel by viewModel<SearchViewModel>()

    private val themePickerViewModel by viewModel<ThemePickerViewModel>()

    private val homePages by lazy {
        listOf(
            HomePage.Main(mainViewModel),
            HomePage.Library(libraryViewModel),
            HomePage.Search(searchViewModel),
        )
    }

    private val screens = listOf(
        DrawerScreen.Home,
        DrawerScreen.Settings,
        DrawerScreen.Theme,
    )

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
                    MainContent(screens, homePages)
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
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        handlePermissionsResult(requestCode, permissions, grantResults)

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @Composable
    fun MainContent(screens: List<DrawerScreen>, pages: List<HomePage>) {
        val scope = rememberCoroutineScope()
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
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
                scaffoldState = rememberScaffoldState(drawerState = drawerState),
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
                NavHost(navController, startDestination = DrawerScreen.Home.route) {
                    composable(DrawerScreen.Home.route) {
                        HomeScreen(
                            onBack = {
                                scope.launch {
                                    drawerState.open()
                                }
                            },
                            pages = pages,
                            controllerViewModel,
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
                            themePickerViewModel,
                        )
                    }
                }
            }
        }
    }
}