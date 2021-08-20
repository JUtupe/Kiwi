package pl.jutupe.main.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.RectangleShape
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.jutupe.home.ui.HomePage
import pl.jutupe.home.ui.HomeScreen
import pl.jutupe.home.ui.controller.BottomMediaControllerViewModel
import pl.jutupe.home.ui.library.LibraryViewModel
import pl.jutupe.home.ui.main.MainViewModel
import pl.jutupe.home.ui.search.SearchViewModel
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

    private val mainViewModel by viewModel<MainViewModel>()
    private val libraryViewModel by viewModel<LibraryViewModel>()
    private val searchViewModel by viewModel<SearchViewModel>()

    private val themePickerViewModel by viewModel<ThemePickerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // todo remove after koin fix
        val homePages = listOf(
            HomePage.Main(mainViewModel),
            HomePage.Library(libraryViewModel),
            HomePage.Search(searchViewModel),
        )

        val screens = listOf(
            DrawerScreen.Home,
            DrawerScreen.Settings,
            DrawerScreen.Theme,
        )

        setContent {
            val navController = rememberNavController()
            val scope = rememberCoroutineScope()
            val currentTheme by themeDataStore.currentThemeFlow.collectAsState(KiwiTheme.Dark)
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val currentRoute = navBackStackEntry?.destination?.route ?: ""

            KiwiTheme(theme = currentTheme) {
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
                                pages = homePages,
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
}