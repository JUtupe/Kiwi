package pl.jutupe.home.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import org.koin.androidx.compose.getViewModel
import pl.jutupe.home.R
import pl.jutupe.home.ui.controller.BottomMediaController
import pl.jutupe.model.MediaItem
import pl.jutupe.ui.items.ArtistItem
import pl.jutupe.ui.items.PlayableItem
import pl.jutupe.ui.util.BackdropHeader

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = getViewModel()
) {
    val recentlyAdded: LazyPagingItems<MediaItem> = viewModel.recentlyAdded.collectAsLazyPagingItems()
    val artists: LazyPagingItems<MediaItem> = viewModel.artists.collectAsLazyPagingItems()
    val allWelcomeMessages = stringArrayResource(id = R.array.welcome_message)
    val welcomeMessage = remember { allWelcomeMessages.random() }

    Surface(
        shape = MaterialTheme.shapes.large
    ) {
        Column(Modifier.fillMaxSize()) {
            BackdropHeader(title = welcomeMessage)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = spacedBy(8.dp),
            ) {
                item {
                    SectionLabel(stringResource(id = R.string.label_random_artists))

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = spacedBy(8.dp),
                        contentPadding = PaddingValues(8.dp),
                    ) {
                        items(artists) { item ->
                            ArtistItem(
                                modifier = Modifier
                                    .widthIn(max = 150.dp),
                                artist = item as MediaItem.Artist,
                                onClick = { },
                            )
                        }
                    }
                }

                item {
                    SectionLabel(stringResource(id = R.string.label_recently_added))

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = spacedBy(8.dp),
                        contentPadding = PaddingValues(8.dp),
                    ) {
                        items(recentlyAdded) { item ->
                            PlayableItem(
                                modifier = Modifier
                                    .size(150.dp),
                                item = item!!,
                                onClick = { viewModel.onSongClicked(item) }
                            )
                        }
                    }
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
fun SectionLabel(label: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        text = label,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h6,
    )
}