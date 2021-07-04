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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import pl.jutupe.model.MediaItem
import pl.jutupe.model.MediaItemAction
import pl.jutupe.ui.R
import pl.jutupe.ui.util.mediaItemRequestBuilder

//todo
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchItem(item: MediaItem, action: MediaItemAction) {
    Card(
        shape = RoundedCornerShape(dimensionResource(R.dimen.radius_small)),
        onClick = { action.onClick(item) }
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(70.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Image(
                painter = rememberCoilPainter(
                    request = item.art,
                    requestBuilder = { apply(mediaItemRequestBuilder(item)) },
                ),
                contentDescription = null,
                Modifier
                    .width(70.dp)
                    .height(70.dp),
            )
            Column(
                Modifier.fillMaxWidth()
            ) {
                Text(text = item.title, maxLines = 1)
                Text(text = item.subtitle, maxLines = 1)
            }
            IconButton(onClick = { action.onMoreClick(item) }) {
                Icon(Icons.Rounded.MoreVert, contentDescription = null, tint = Color.Black)
            }
        }
    }
}