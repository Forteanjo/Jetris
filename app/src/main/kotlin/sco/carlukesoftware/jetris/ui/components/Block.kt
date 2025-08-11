package sco.carlukesoftware.jetris.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.annotations.Blocking
import sco.carlukesoftware.jetris.ui.theme.JetrisTheme
import sco.carlukesoftware.jetris.ui.theme.TetrisGridBorderColor
import sco.carlukesoftware.jetris.ui.theme.TetrisGridColor

@Composable
fun Block(
    size: Dp = 50.dp,
    borderWidth: Dp = 2.dp,
    blockColor: Color = TetrisGridColor,
    borderColor: Color = TetrisGridBorderColor
) {
    Box(
        modifier = Modifier
            .size(size) // You can omit "size =" if the parameter name matches
            .border(
                width = borderWidth,
                color = borderColor,
                shape = MaterialTheme.shapes.small
            )
            .background(
                color = blockColor, // Set the actual block color here
                shape = MaterialTheme.shapes.small
            )
    )
}

@Preview(showBackground = true)
@Composable
private fun BlockPreview() {
    JetrisTheme {
        Block()
    }
}
