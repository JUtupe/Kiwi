package pl.jutupe.home.ui.controller

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.jutupe.home.R
import pl.jutupe.model.MediaItem
import pl.jutupe.ui.theme.KiwiTheme
import pl.jutupe.ui.util.mediaItemPainter

object BottomMediaController {
    val CONTROLLER_HEIGHT = 70.dp
}

// todo swipe gestures listener
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun BottomMediaController(
    modifier: Modifier = Modifier,
    currentItem: MediaItem,
    isPlaying: Boolean = false,
    onPlayPauseClicked: () -> Unit = { },
) {
    Box(
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .height(BottomMediaController.CONTROLLER_HEIGHT)
            .background(Color.Transparent)
    ) {
        Card(
            shape = MaterialTheme.shapes.small,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .size(BottomMediaController.CONTROLLER_HEIGHT),
                    contentScale = ContentScale.Crop,
                    painter = mediaItemPainter(currentItem),
                    contentDescription = null,
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                ) {
                    Text(text = currentItem.title)

                    Text(text = currentItem.subtitle)
                }

                IconToggleButton(
                    checked = isPlaying,
                    onCheckedChange = { onPlayPauseClicked() },
                    modifier = Modifier
                        .size(56.dp)
                        .padding(8.dp),
                ) {
                    if (isPlaying) {
                        Icon(painterResource(id = R.drawable.ic_pause), null)
                    } else {
                        Icon(painterResource(id = R.drawable.ic_play), null)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun BottomMediaControllerPreview() {
    val item = MediaItem.Song("id", "title", "artist", null)

    KiwiTheme {
        BottomMediaController(
            modifier = Modifier
                .width(500.dp),
            currentItem = item,
        )
    }
}