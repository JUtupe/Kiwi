package pl.jutupe.playback

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import pl.jutupe.model.MediaItem
import pl.jutupe.ui.theme.KiwiTheme
import pl.jutupe.ui.util.BackdropHeader
import pl.jutupe.ui.util.mediaItemPainter
import kotlin.math.roundToInt

@Composable
fun PlaybackScreen(
    onBack: () -> Unit,
    viewModel: PlaybackViewModel = getViewModel(),
) {
    val nowPlaying by viewModel.nowPlaying.observeAsState()
    val isPlaying by viewModel.isPlaying.observeAsState(false)

    PlaybackContent(
        modifier = Modifier
            .fillMaxSize(),
        nowPlaying = nowPlaying,
        isPlaying = isPlaying,
        onBack = { onBack() },
        onPlayPauseClicked = { viewModel.onPlayPauseClicked() },
        onSkipToNextClicked = { viewModel.onSkipToNextClicked() },
        onSkipToPrevClicked = { viewModel.onSkipToPreviousClicked() }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlaybackContent(
    modifier: Modifier = Modifier,
    previousSong: MediaItem? = null,
    nowPlaying: MediaItem? = null,
    nextSong: MediaItem? = null,
    isPlaying: Boolean = false,
    onBack: () -> Unit = { },
    onPlayPauseClicked: () -> Unit = { },
    onSkipToNextClicked: () -> Unit = { },
    onSkipToPrevClicked: () -> Unit = { },
) {
    val swipeableState = rememberSwipeableState(0)
    val endAnchor = LocalConfiguration.current.screenHeightDp * LocalDensity.current.density
    val anchors = mapOf(
        0f to 0,
        endAnchor to 1
    )

    Surface(
        modifier = modifier
            .fillMaxSize()
            .offset { IntOffset(0, swipeableState.offset.value.roundToInt()) }
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                orientation = Orientation.Vertical,
                thresholds = { _, _ -> FractionalThreshold(0.34f) },
            ),
        shape = MaterialTheme.shapes.large,
    ) {
        if (swipeableState.currentValue >= 1) {
            LaunchedEffect("playback") {
                onBack()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colors.surface,
                            Color.Black,
                        )
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            BackdropHeader(
                startIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                    }
                },
                title = "",
            )

            //todo carousel
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                nowPlaying?.let { mediaItemPainter(item = it) }
                    ?: painterResource(id = R.drawable.placeholder_song),
                null,
                modifier = Modifier.size(200.dp),
            )
            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = nowPlaying?.title ?: "",
                )

                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = nowPlaying?.subtitle ?: "",
                )

                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = "album",
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth(),
            ) {
                LinearProgressIndicator()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(text = "00:00")

                    Text(text = "11:11")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier
                    .height(80.dp)
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = { onSkipToPrevClicked() }) {
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = null)
                    }

                    IconToggleButton(
                        checked = isPlaying,
                        onCheckedChange = { onPlayPauseClicked() },
                    ) {
                        if (isPlaying) {
                            Icon(painterResource(id = R.drawable.ic_pause), null)
                        } else {
                            Icon(painterResource(id = R.drawable.ic_play), null)
                        }
                    }

                    IconButton(onClick = { onSkipToNextClicked() }) {
                        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PlaybackContentPreview() {
    KiwiTheme {
        PlaybackContent(
            nowPlaying = MediaItem.Song(
                id = "",
                title = "Some Short Title",
                artist = "Example Artist",
                art = null,
                duration = 1200,
            )
        )
    }
}

@Preview
@Composable
fun PlaybackContentPreviewLight() {
    KiwiTheme(theme = KiwiTheme.Light) {
        PlaybackContent(
            nowPlaying = MediaItem.Song(
                id = "",
                title = "Some Short Title",
                artist = "Example Artist",
                art = null,
                duration = 1200,
            )
        )
    }
}