package pl.jutupe.main.ui.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.jutupe.main.R
import pl.jutupe.ui.theme.KiwiTheme

//todo fix ripple effect recomposition
@Composable
fun KiwiDrawer(
    screens: List<DrawerScreen>,
    currentRoute: String,
    onClick: (DrawerScreen) -> Unit = { },
) {
    val context = LocalContext.current
    val versionName = remember {
        context.packageManager
            .getPackageInfo(context.packageName, 0)
            .versionName
    }

    val buttonShape = RoundedCornerShape(
        topEndPercent = 50,
        bottomEndPercent = 50,
    )

    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.surface)
            .fillMaxSize(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = pl.jutupe.ui.R.drawable.ic_launcher_foreground),
                contentDescription = null,
            )

            Column {
                Text(
                    text = stringResource(id = R.string.app_name)
                        .uppercase(),
                    fontSize = 32.sp,
                )

                Text(
                    text = versionName
                )
            }
        }

        Spacer(
            modifier = Modifier
                .height(32.dp),
        )

        screens.forEach { screen ->
            Text(
                text = stringResource(id = screen.titleRes)
                    .uppercase(),
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(end = 100.dp)
                    .fillMaxWidth()
                    .offset(x = (-3).dp)
                    .clip(shape = buttonShape)
                    .then(
                        if (currentRoute == screen.route) {
                            Modifier.border(
                                width = 1.5.dp,
                                color = Color.White,
                                shape = buttonShape,
                            )
                        } else Modifier
                    )
                    .clickable { onClick(screen) }
                    .padding(
                        start = 32.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    ),
            )

            Spacer(
                modifier = Modifier
                    .height(8.dp),
            )
        }
    }
}

sealed class DrawerScreen(
    val titleRes: Int,
    val route: String
) {

    object Home : DrawerScreen(
        titleRes = R.string.menu_home,
        route = "home"
    )

    object Settings : DrawerScreen(
        titleRes = R.string.menu_settings,
        route = "settings"
    )
}

@Preview
@Composable
private fun KiwiDrawerPreview() {
    val screens = listOf(
        DrawerScreen.Home,
        DrawerScreen.Settings,
    )

    KiwiTheme {
        KiwiDrawer(
            screens = screens,
            currentRoute = screens.first().route
        )
    }
}