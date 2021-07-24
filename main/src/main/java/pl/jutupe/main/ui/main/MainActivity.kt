package pl.jutupe.main.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.RectangleShape
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.jutupe.home.ui.HomePage
import pl.jutupe.home.ui.HomeScreen
import pl.jutupe.home.ui.controller.BottomMediaControllerViewModel
import pl.jutupe.home.ui.library.LibraryViewModel
import pl.jutupe.home.ui.main.MainViewModel
import pl.jutupe.home.ui.search.SearchViewModel
import pl.jutupe.main.ui.drawer.DrawerScreen
import pl.jutupe.main.ui.drawer.KiwiDrawer
import pl.jutupe.ui.theme.KiwiTheme

class MainActivity : ComponentActivity() {

    private val controllerViewModel by viewModel<BottomMediaControllerViewModel>()

    private val mainViewModel by viewModel<MainViewModel>()
    private val libraryViewModel by viewModel<LibraryViewModel>()
    private val searchViewModel by viewModel<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //todo remove after koin fix
        val homePages = listOf(
            HomePage.Main(mainViewModel),
            HomePage.Library(libraryViewModel),
            HomePage.Search(searchViewModel),
        )

        val screens = listOf(
            DrawerScreen.Home,
            DrawerScreen.Settings,
        )

        setContent {
            val navController = rememberNavController()
            val scope = rememberCoroutineScope()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val currentRoute = navBackStackEntry?.destination?.route ?: ""

            KiwiTheme {
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
                                controllerViewModel
                            )
                        }

                        composable(DrawerScreen.Settings.route) {
                            Text(text = "test")
                        }
                    }
                }
            }
        }
    }
}