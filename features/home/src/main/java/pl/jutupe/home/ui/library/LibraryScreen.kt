package pl.jutupe.home.ui.library

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import org.koin.androidx.compose.getViewModel
import pl.jutupe.core.browser.LocalMediaBrowserTree.Companion.KIWI_ROOT_ARTISTS
import pl.jutupe.core.browser.MediaBrowserTree.Companion.KIWI_MEDIA_ROOT
import pl.jutupe.home.ui.controller.BottomMediaController
import pl.jutupe.model.MediaItem
import pl.jutupe.ui.items.ArtistItem
import pl.jutupe.ui.items.PlayableItem
import pl.jutupe.ui.items.RootItem
import pl.jutupe.ui.util.BackdropHeader

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = getViewModel()
) {
    val libraryItems = viewModel.items.collectAsLazyPagingItems()
    val isInRoot: Boolean by viewModel.isInRoot.collectAsState()
    val currentRoot: MediaItem by viewModel.getCurrentRoot().collectAsState()

    BackHandler(!isInRoot) {
        viewModel.onNavigateToParentClicked()
    }
    
    Surface(
        shape = MaterialTheme.shapes.large
    ) {
        Column(Modifier.fillMaxSize()) {
            BackdropHeader(
                startIcon = {
                    AnimatedVisibility(
                        visible = !isInRoot,
                        enter = slideInHorizontally(),
                        exit = slideOutHorizontally(),
                    ) {
                        IconButton(onClick = {
                            viewModel.onNavigateToParentClicked()
                        }) {
                            Icon(Icons.Rounded.KeyboardArrowLeft, null)
                        }
                    }
                },
                title = currentRoot.title,
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp),
            ) {
                items(libraryItems) { item ->
                    PlayableItem(
                        modifier = Modifier
                            .size(200.dp),
                        item = item!!,
                        onClick = { viewModel.onSongClicked(item) },
                        onMoreClick = { viewModel.onSongMoreClick(item) },
                        moreButtonVisible = true,
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
    }
}

@Composable
fun ItemForParent(
    parentId: String,
    item: MediaItem,
    onClick: (MediaItem) -> Unit = { },
    onMoreClick: (MediaItem) -> Unit = { },
) {
    when (parentId) {
        KIWI_MEDIA_ROOT ->
            RootItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                item = item,
                onClick = onClick,
            )
        KIWI_ROOT_ARTISTS ->
            ArtistItem(artist = item as MediaItem.Artist, onClick = onClick)
        else ->
            PlayableItem(
                item = item,
                onClick = onClick,
                onMoreClick = onMoreClick,
                moreButtonVisible = true,
            )
    }
}

fun columnsForParent(parentId: String): Int =
    when (parentId) {
        KIWI_MEDIA_ROOT -> 1
        KIWI_ROOT_ARTISTS -> 3
        else -> 2
    }