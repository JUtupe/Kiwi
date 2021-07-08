package pl.jutupe.home.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun Header(
    title: String,
    startIcon: @Composable () -> Unit = { },
    endIcon: @Composable () -> Unit = { },
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(Header.HEADER_HEIGHT)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        startIcon()

        Text(
            modifier = Modifier
                .padding(8.dp)
                .weight(1f),
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start
        )

        endIcon()
    }
}

object Header {
    val HEADER_HEIGHT = 64.dp
}