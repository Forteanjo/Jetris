package sco.carlukesoftware.jetris.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
                shape = MaterialTheme.shapes.extraSmall
            )
            .background(
                color = blockColor, // Set the actual block color here
                shape = MaterialTheme.shapes.extraSmall
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
