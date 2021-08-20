package pl.jutupe.theme.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import pl.jutupe.theme.R
import pl.jutupe.ui.theme.KiwiTheme
import pl.jutupe.ui.util.AppBarTitle

@Composable
fun ThemePickerScreen(
    onBack: () -> Unit = { },
    viewModel: ThemePickerViewModel = getViewModel(),
) {
    val scope = rememberCoroutineScope()
    val themes = remember { viewModel.getAllThemes() }
    val currentTheme by viewModel.currentTheme.collectAsState(KiwiTheme.Dark)

    ThemePickerContent(
        onBack = onBack,
        themes = themes,
        onThemeClicked = { theme ->
            scope.launch {
                viewModel.onThemeClicked(theme)
            }
        },
        currentTheme = currentTheme,
    )
}

@Composable
fun ThemePickerContent(
    onBack: () -> Unit = { },
    themes: List<KiwiTheme>,
    onThemeClicked: (KiwiTheme) -> Unit = { },
    currentTheme: KiwiTheme,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { AppBarTitle(stringResource(id = R.string.tab_theme)) },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Rounded.Menu, null, tint = Color.White)
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onSurface,
                elevation = 0.dp,
            )
        },
        backgroundColor = MaterialTheme.colors.primary,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            shape = MaterialTheme.shapes.large,
        ) {
            Column {
                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = spacedBy(8.dp),
                ) {
                    items(themes) { theme ->
                        KiwiTheme(theme) {
                            ThemeItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp),
                                name = theme.getThemeName(),
                                onSelected = { onThemeClicked(theme) },
                                isSelected = currentTheme == theme
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(locale = "pl")
private fun ThemePickerContentPreview() {
    KiwiTheme {
        ThemePickerContent(
            themes = listOf(
                KiwiTheme.Dark,
                KiwiTheme.Light,
            ),
            currentTheme = KiwiTheme.Light,
        )
    }
}