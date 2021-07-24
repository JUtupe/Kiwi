package pl.jutupe.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import pl.jutupe.home.R
import pl.jutupe.home.ui.controller.BottomMediaController
import pl.jutupe.home.ui.controller.BottomMediaControllerViewModel
import pl.jutupe.home.ui.library.LibraryScreen
import pl.jutupe.home.ui.library.LibraryViewModel
import pl.jutupe.home.ui.main.MainScreen
import pl.jutupe.home.ui.main.MainViewModel
import pl.jutupe.home.ui.search.SearchScreen
import pl.jutupe.home.ui.search.SearchViewModel

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    onBack: () -> Unit = { },
    pages: List<HomePage> = emptyList(),
    bottomMediaControllerViewModel: BottomMediaControllerViewModel = getViewModel(),
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        pageCount = 3,
        initialOffscreenLimit = 2
    )
    val scaffoldState = rememberBottomSheetScaffoldState()

    val isPlaying by bottomMediaControllerViewModel.isPlaying.observeAsState()
    val nowPlaying by bottomMediaControllerViewModel.nowPlaying.observeAsState()

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
                        Icon(Icons.Rounded.Menu, null)
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onSurface,
                elevation = 0.dp,
            )
        },
        sheetPeekHeight = 0.dp,
        sheetContent = {
            nowPlaying?.let { itemPlaying ->
                BottomMediaController(
                    modifier = Modifier
                        .height(70.dp)
                        .fillMaxWidth(),
                    currentItem = itemPlaying,
                    isPlaying = isPlaying == true,
                    onPlayPauseClicked = { bottomMediaControllerViewModel.onPlayPauseClicked() }
                )
            }
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

    class Main(viewModel: MainViewModel) : HomePage(
        content = { MainScreen(viewModel) },
        titleRes = R.string.tab_main,
    )

    class Library(viewModel: LibraryViewModel) : HomePage(
        content = { LibraryScreen(viewModel) },
        titleRes = R.string.tab_library,
    )

    class Search(viewModel: SearchViewModel) : HomePage(
        content = { SearchScreen(viewModel) },
        titleRes = R.string.tab_search,
    )
}