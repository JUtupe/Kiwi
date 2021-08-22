package pl.jutupe.ui.util

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppBarTitle(title: String) {
    Box(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .fillMaxHeight()
            .border(
                width = 1.5.dp,
                color = Color.White,
                shape = RoundedCornerShape(percent = 50)
            ),
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 24.dp),
            text = title.uppercase(),
            color = Color.White,
            fontSize = 12.sp,
            letterSpacing = 1.sp
        )
    }
}