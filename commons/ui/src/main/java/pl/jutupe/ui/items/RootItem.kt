package pl.jutupe.ui.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.jutupe.model.MediaItem
import pl.jutupe.ui.R
import pl.jutupe.ui.util.mediaItemPainter

@Composable
fun RootItem(
    modifier: Modifier = Modifier,
    item: MediaItem,
    onClick: (MediaItem) -> Unit = { },
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.radius_medium)))
            .clickable { onClick(item) },
        contentAlignment = Alignment.BottomCenter,
    ) {
        Image(
            modifier = modifier
                .fillMaxSize(),
            painter = mediaItemPainter(item),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )

        Text(
            modifier = Modifier
                .padding(8.dp),
            text = item.title,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.surface,
            fontWeight = FontWeight.Bold,
            fontSize = 60.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.h3,
        )
    }
}