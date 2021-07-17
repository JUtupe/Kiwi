package pl.jutupe.ui.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import pl.jutupe.model.MediaItem
import pl.jutupe.ui.DARK_GRADIENT
import pl.jutupe.ui.util.mediaItemPainter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlayableItem(
    modifier: Modifier = Modifier,
    item: MediaItem,
    onClick: (MediaItem) -> Unit = { },
    moreButtonVisible: Boolean = false,
    onMoreClick: (MediaItem) -> Unit = { },
) {
    Card(
        modifier = modifier,
        onClick = { onClick(item) },
    ) {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
                painter = mediaItemPainter(item),
                contentDescription = null,
            )

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight(Alignment.Bottom)
                    .background(DARK_GRADIENT),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = item.title,
                            textAlign = TextAlign.Start,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )

                        Text(
                            text = item.subtitle,
                            textAlign = TextAlign.Start,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                    }

                    if (moreButtonVisible) {
                        IconButton(onClick = { onMoreClick(item) }) {
                            Icon(Icons.Rounded.MoreVert, null)
                        }
                    }
                }
            }
        }
    }
}