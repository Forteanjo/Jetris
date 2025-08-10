package sco.carlukesoftware.jetris.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sco.carlukesoftware.jetris.ui.theme.JetrisTheme
import sco.carlukesoftware.jetris.ui.theme.PlanetBe

@Composable
fun CharBlock(
    size: Dp = 100.dp,
    borderWidth: Dp = 4.dp,
    blockColor: Color,
    borderColor: Color,
    textColor: Color = Color.Black,
    char: Char
) {
    Box(
        modifier = Modifier
            .background(Color.Transparent),
    ) {
        Block(
            size = size,
            borderWidth = borderWidth,
            blockColor = blockColor,
            borderColor = borderColor
        )

        Box(
            modifier = Modifier
                .size(size)
                .offset(y = (-10).dp),
            contentAlignment = Alignment.Center // Centers its direct child (the Text)
        ) {
            Text(
                text = char.toString(),
                color = textColor,
                modifier = Modifier
                    .width(size + 10.dp)
                    .height(size + 10.dp),
                textAlign = TextAlign.Center,
                fontSize = (size.value / 1.4).sp,
                fontFamily = PlanetBe,
                lineHeight = (size.value + 20).sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CharBlockPreview() {
    JetrisTheme {
        CharBlock(
            blockColor = Color.Red,
            borderColor = Color.Black,
            char = 'A'
        )
    }
}
