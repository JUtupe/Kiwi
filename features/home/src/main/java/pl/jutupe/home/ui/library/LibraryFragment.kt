package pl.jutupe.home.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import kotlinx.coroutines.flow.collect
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.jutupe.core.browser.LocalMediaBrowserTree.Companion.KIWI_ROOT_ARTISTS
import pl.jutupe.core.browser.MediaBrowserTree.Companion.KIWI_MEDIA_ROOT
import pl.jutupe.home.ui.Header
import pl.jutupe.model.MediaItem
import pl.jutupe.ui.items.ArtistItem
import pl.jutupe.ui.items.PlayableItem
import pl.jutupe.ui.items.RootItem
import pl.jutupe.ui.theme.KiwiTheme

class LibraryFragment : Fragment() {

    val viewModel by viewModel<LibraryViewModel>()

    private val libraryBackCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            viewModel.onNavigateToParentClicked()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this, libraryBackCallback)

        lifecycleScope.launchWhenCreated {
            viewModel.isInRoot.collect { isRoot ->
                libraryBackCallback.isEnabled = !isRoot
            }
        }
    }

    override fun onPause() {
        super.onPause()

        libraryBackCallback.isEnabled = false
    }

    override fun onResume() {
        super.onResume()

        libraryBackCallback.isEnabled = viewModel.isInRoot.value == false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            KiwiTheme {
                LibraryContent(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun LibraryContent(
    viewModel: LibraryViewModel = getViewModel()
) {
    val libraryItems = viewModel.items.collectAsLazyPagingItems()
    val isInRoot: Boolean by viewModel.isInRoot.collectAsState()
    val currentRoot: MediaItem by viewModel.getCurrentRoot().collectAsState()

    Surface(
        shape = MaterialTheme.shapes.large
    ) {
        Column(Modifier.fillMaxSize()) {
            Header(
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
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = spacedBy(8.dp),
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