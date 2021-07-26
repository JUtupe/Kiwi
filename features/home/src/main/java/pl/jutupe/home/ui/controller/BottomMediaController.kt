package pl.jutupe.home.ui.controller

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import pl.jutupe.home.R
import pl.jutupe.model.MediaItem
import pl.jutupe.ui.util.mediaItemPainter

object BottomMediaController {
    val CONTROLLER_HEIGHT = 70.dp
}

//todo all sides padding
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun BottomMediaController(
    modifier: Modifier = Modifier,
    currentItem: MediaItem,
    isPlaying: Boolean = false,
    onPlayPauseClicked: () -> Unit = { },
) {
    Card(
        modifier = modifier
            .height(BottomMediaController.CONTROLLER_HEIGHT),
        shape = RectangleShape,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier
                    .size(70.dp),
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

            FloatingActionButton(
                modifier = Modifier
                    .size(56.dp)
                    .padding(8.dp),
                onClick = { onPlayPauseClicked() },
                backgroundColor = MaterialTheme.colors.primary,
            ) {
                Crossfade(targetState = isPlaying) { playing ->
                    if (playing) {
                        Icon(painterResource(id = R.drawable.ic_pause), null)
                    } else {
                        Icon(painterResource(id = R.drawable.ic_play), null)
                    }
                }
            }
        }
    }
}