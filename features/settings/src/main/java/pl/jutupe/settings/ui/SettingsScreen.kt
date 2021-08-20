package pl.jutupe.settings.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pl.jutupe.settings.R
import pl.jutupe.ui.util.AppBarTitle

@Composable
fun SettingsScreen(
    onBack: () -> Unit = { }
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { AppBarTitle(stringResource(id = R.string.tab_settings)) },
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

        }
    }
}