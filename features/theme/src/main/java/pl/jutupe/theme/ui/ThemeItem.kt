package pl.jutupe.theme.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.jutupe.ui.theme.KiwiTheme

@Composable
fun ThemeItem(
    modifier: Modifier = Modifier,
    name: String,
    onSelected: () -> Unit = { },
    isSelected: Boolean = false,
) {
    val colors = listOf(
        MaterialTheme.colors.primary,
        MaterialTheme.colors.primaryVariant,
        MaterialTheme.colors.secondary,
        MaterialTheme.colors.secondaryVariant,
        MaterialTheme.colors.background,
        MaterialTheme.colors.surface,
    )

    Card(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .clickable { onSelected() }
                .fillMaxSize()
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = MaterialTheme.shapes.medium,
                ),
            contentAlignment = Alignment.TopStart,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row {
                    RadioButton(
                        selected = isSelected,
                        onClick = { onSelected() },
                    )

                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = name.uppercase(),
                        fontWeight = FontWeight.Bold,
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                LazyRow(
                    horizontalArrangement = spacedBy(10.dp)
                ) {
                    items(colors) { color ->
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(color, CircleShape)
                                .border(1.dp, Color.Black, CircleShape)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ThemeItemPreview() {
    KiwiTheme {
        ThemeItem(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            name = "Test",
        )
    }
}

@Preview
@Composable
private fun ThemeItemPreviewLight() {
    KiwiTheme(theme = KiwiTheme.Light) {
        ThemeItem(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            name = "Test",
        )
    }
}