package pl.jutupe.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import pl.jutupe.home.R
import pl.jutupe.home.ui.controller.BottomMediaController
import pl.jutupe.home.ui.controller.BottomMediaControllerViewModel
import pl.jutupe.home.ui.library.LibraryScreen
import pl.jutupe.home.ui.main.MainScreen
import pl.jutupe.home.ui.search.SearchScreen

@OptIn(
    ExperimentalPagerApi::class,
    ExperimentalMaterialApi::class,
)
@Composable
fun HomeScreen(
    onBack: () -> Unit = { },
    onShowPlayback: () -> Unit = { },
    pages: List<HomePage> = emptyList(),
    bottomMediaControllerViewModel: BottomMediaControllerViewModel = getViewModel(),
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        pageCount = pages.size,
        initialOffscreenLimit = 2
    )
    val scaffoldState = rememberBottomSheetScaffoldState()

    LaunchedEffect(scaffoldState.bottomSheetState) {
        snapshotFlow { scaffoldState.bottomSheetState.isCollapsed }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                bottomMediaControllerViewModel.onDownSwiped()
            }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    TabRow(
                        backgroundColor = MaterialTheme.colors.primary,
                        selectedTabIndex = pagerState.currentPage,
                        divider = { },
                        indicator = { tabPositions ->
                            Box(
                                modifier = Modifier
                                    .pagerTabIndicatorOffset(pagerState, tabPositions)
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                                    .fillMaxHeight()
                                    .border(
                                        width = 1.5.dp,
                                        color = Color.White,
                                        shape = RoundedCornerShape(percent = 50)
                                    ),
                            )
                        }
                    ) {
                        pages.forEachIndexed { index, page ->
                            Tab(
                                modifier = Modifier
                                    .padding(vertical = 12.dp)
                                    .clip(RoundedCornerShape(percent = 50)),
                                text = {
                                    Text(
                                        text = stringResource(id = page.titleRes)
                                            .uppercase(),
                                        fontSize = 12.sp
                                    )
                                },
                                selected = pagerState.currentPage == index,
                                selectedContentColor = Color.White,
                                unselectedContentColor = Color.White,
                                onClick = {
                                    scope.launch {
                                        pagerState.scrollToPage(index)
                                    }
                                },
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Rounded.Menu, null, tint = Color.White)
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onSurface,
                elevation = 0.dp,
            )
        },
        sheetPeekHeight = 0.dp,
        sheetBackgroundColor = Color.Transparent,
        sheetShape = MaterialTheme.shapes.large,
        sheetElevation = 0.dp,
        sheetContent = {
            BottomMediaController(
                onPlaybackOpenClicked = { onShowPlayback() }
            )
        },
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .background(MaterialTheme.colors.primary),
            itemSpacing = dimensionResource(id = R.dimen.margin_fragment),
        ) { pageIndex ->
            pages[pageIndex].content()
        }
    }
}

sealed class HomePage(
    val content: @Composable () -> Unit,
    val titleRes: Int,
) {

    class Main : HomePage(
        content = { MainScreen() },
        titleRes = R.string.tab_main,
    )

    class Library : HomePage(
        content = { LibraryScreen() },
        titleRes = R.string.tab_library,
    )

    class Search : HomePage(
        content = { SearchScreen() },
        titleRes = R.string.tab_search,
    )
}