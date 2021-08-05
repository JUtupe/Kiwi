package pl.jutupe.ui.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.jutupe.model.MediaItem
import pl.jutupe.ui.ARTIST_BACKGROUND
import pl.jutupe.ui.R
import pl.jutupe.ui.theme.KiwiTheme
import pl.jutupe.ui.util.mediaItemPainter

@Composable
fun ArtistItem(
    modifier: Modifier = Modifier,
    artist: MediaItem.Artist,
    onClick: (MediaItem.Artist) -> Unit = { },
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
                .height(100.dp)
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.radius_medium)))
                .clickable { onClick(artist) },
        ) {
            Text(
                modifier = Modifier
                    .background(brush = ARTIST_BACKGROUND)
                    .padding(8.dp)
                    .padding(top = 30.dp),
                text = artist.artist,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        Image(
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
                .clip(CircleShape)
                .clickable { onClick(artist) },
            painter = mediaItemPainter(artist),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )
    }
}

@Composable
@Preview
private fun ArtistItemPreview() {
    KiwiTheme {
        ArtistItem(
            modifier = Modifier
                .height(150.dp)
                .width(150.dp),
            artist = MediaItem.Artist("", "title", "Artist artist artist aaaaa aaa", null),
        )
    }
}