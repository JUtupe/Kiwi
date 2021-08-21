package pl.jutupe.home.ui.search

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import pl.jutupe.home.R
import pl.jutupe.home.ui.controller.BottomMediaController
import pl.jutupe.model.MediaItem
import pl.jutupe.ui.items.SearchItem
import pl.jutupe.ui.util.BackdropHeader

// todo scrim color
@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class, ExperimentalUnitApi::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = getViewModel(),
) {
    val backdropState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    val modelSearchItems: LazyPagingItems<MediaItem> = viewModel.items.collectAsLazyPagingItems()

    val composeScope = rememberCoroutineScope()

    val searchFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current

    val headerQuery by viewModel.getCurrentQuery().collectAsState("")
    val inputQuery by viewModel.searchQuery.collectAsState("")

    LaunchedEffect(backdropState) {
        snapshotFlow { backdropState.isConcealed }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                searchFocusRequester.freeFocus()
                localFocusManager.clearFocus(true)
                keyboardController?.hide()
                backdropState.conceal()
            }
    }

    BackdropScaffold(
        scaffoldState = backdropState,
        appBar = {},
        peekHeight = 0.dp,
        headerHeight = BackdropHeader.HEADER_HEIGHT,
        backLayerContent = {
            TextField(
                modifier = Modifier
                    .focusRequester(searchFocusRequester)
                    .fillMaxWidth()
                    .padding(8.dp),
                value = inputQuery,
                onValueChange = { value ->
                    viewModel.searchQuery.value = value
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.hint_search),
                        color = Color.Gray,
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(percent = 100),
                trailingIcon = {
                    Icon(Icons.Rounded.Close,
                        modifier = Modifier
                            .clickable { viewModel.onSearchClearClicked() },
                        contentDescription = null,
                        tint = Color.Black
                    )
                },
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() },
                ),
                colors = textFieldColors(
                    textColor = Color.Black,
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
            )
        },
        frontLayerScrimColor = Color.Transparent,
        frontLayerShape = MaterialTheme.shapes.large,
        frontLayerElevation = 0.dp,
        frontLayerContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.surface)
            ) {
                BackdropHeader(
                    title = headerForQuery(headerQuery),
                    endIcon = {
                        Crossfade(targetState = backdropState.targetValue) {
                            when (it) {
                                BackdropValue.Revealed ->
                                    IconButton(onClick = { /* handled by effect */ }) {
                                        Icon(Icons.Rounded.KeyboardArrowUp, null)
                                    }
                                BackdropValue.Concealed ->
                                    IconButton(onClick = {
                                        composeScope.launch {
                                            searchFocusRequester.requestFocus()
                                            keyboardController?.show()
                                            backdropState.reveal()
                                        }
                                    }) {
                                        Icon(Icons.Rounded.Search, null)
                                    }
                            }
                        }
                    }
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(8.dp),
                ) {
                    items(modelSearchItems) { searchItem ->
                        SearchItem(
                            item = searchItem!!,
                            onClick = { viewModel.onSearchItemClicked(it) },
                            onMoreClick = { viewModel.onSearchItemMoreClicked(it) },
                        )
                    }

                    item {
                        Spacer(
                            modifier = Modifier
                                .height(BottomMediaController.CONTROLLER_HEIGHT)
                        )
                    }
                }
            }
        },
    )
}

@Composable
private fun headerForQuery(query: String): String =
    if (query.isBlank()) stringResource(R.string.label_recently_searched)
    else stringResource(R.string.label_search_result_for, query)