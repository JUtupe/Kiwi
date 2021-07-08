package pl.jutupe.home.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import pl.jutupe.home.R
import pl.jutupe.home.ui.Header
import pl.jutupe.model.MediaItem
import pl.jutupe.ui.items.SearchItem
import pl.jutupe.ui.theme.KiwiTheme

class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        val viewModel = getViewModel<SearchViewModel>()

        setContent {
            KiwiTheme {
                SearchContent(viewModel)
            }
        }
    }
}

//todo fix bottom space on drag
//todo fix keyboard focus (two onclick in one time)
@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchContent(
    viewModel: SearchViewModel = getViewModel(),
) {
    val backdropState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    val modelSearchItems: LazyPagingItems<MediaItem> = viewModel.items.collectAsLazyPagingItems()

    val composeScope = rememberCoroutineScope()

    val searchFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current

    val headerQuery: String by viewModel.getCurrentQuery().collectAsState("")
    val inputQuery: String by viewModel.searchQuery.collectAsState("")

    BackdropScaffold(
        scaffoldState = backdropState,
        appBar = {},
        peekHeight = 0.dp,
        headerHeight = Header.HEADER_HEIGHT,
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
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
            )
        },
        frontLayerScrimColor = Color.Transparent,
        frontLayerShape = MaterialTheme.shapes.large,
        frontLayerContent = {
            Column(Modifier.fillMaxSize()) {
                Header(
                    title = headerForQuery(headerQuery),
                    endIcon = {
                        Crossfade(targetState = backdropState.targetValue) {
                            when (it) {
                                BackdropValue.Revealed ->
                                    IconButton(onClick = {
                                        composeScope.launch {
                                            localFocusManager.clearFocus()
                                            keyboardController?.hide()
                                            backdropState.conceal()
                                        }
                                    }) { Icon(Icons.Rounded.KeyboardArrowUp, null) }
                                BackdropValue.Concealed ->
                                    IconButton(onClick = {
                                        composeScope.launch {
                                            searchFocusRequester.requestFocus()
                                            keyboardController?.show()
                                            backdropState.reveal()
                                        }
                                    }) { Icon(Icons.Rounded.Search, null) }
                            }
                        }
                    }
                )
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxSize()
                ) {
                    items(modelSearchItems) { searchItem ->
                        SearchItem(
                            item = searchItem!!,
                            onClick = { viewModel.onSearchItemClicked(it) },
                            onMoreClick = { viewModel.onSearchItemMoreClicked(it) },
                        )
                        Spacer(Modifier.height(8.dp))
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