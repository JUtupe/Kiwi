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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import pl.jutupe.home.R
import pl.jutupe.home.ui.controller.BottomMediaController
import pl.jutupe.home.ui.controller.BottomMediaControllerViewModel

@OptIn(
    ExperimentalPagerApi::class,
    ExperimentalMaterialApi::class,
)
@Composable
fun HomeScreen(
    onBack: () -> Unit = { },
) {
    val pages = remember {
        listOf(
            HomePage.Main(),
            HomePage.Library(),
            HomePage.Search(),
        )
    }
    val pagerState = rememberPagerState(
        pageCount = pages.size,
        initialOffscreenLimit = 2
    )
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            HomeTopBar(
                onBack = { onBack() },
                pages = pages,
                pagerState = pagerState,
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

@OptIn(
    ExperimentalPagerApi::class,
    ExperimentalMaterialApi::class,
)
@Composable
private fun HomeTopBar(
    onBack: () -> Unit = { },
    pages: List<HomePage>,
    pagerState: PagerState,
) {
    val scope = rememberCoroutineScope()

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
}