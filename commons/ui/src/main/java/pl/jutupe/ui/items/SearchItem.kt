package pl.jutupe.ui.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.jutupe.model.MediaItem
import pl.jutupe.ui.R
import pl.jutupe.ui.theme.KiwiTheme
import pl.jutupe.ui.util.mediaItemPainter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchItem(
    item: MediaItem,
    onClick: ((MediaItem) -> Unit) = { },
    onMoreClick: ((MediaItem) -> Unit) = { },
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = 4.dp,
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_small)),
        onClick = { onClick(item) }
    ) {
        Row(
            modifier = Modifier
                .height(70.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Image(
                painter = mediaItemPainter(item),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(70.dp)
                    .height(70.dp)
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.radius_small))),
            )
            Spacer(
                modifier = Modifier
                    .padding(start = 8.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = item.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = item.subtitle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(onClick = { onMoreClick(item) }) {
                Icon(
                    Icons.Rounded.MoreVert,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
@Preview
private fun SearchItemPreview() {
    KiwiTheme {
        SearchItem(
            item = MediaItem.Song("", "title", "artist", null),
        )
    }
}