package sco.carlukesoftware.jetris.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sco.carlukesoftware.jetris.ui.theme.JetrisTheme
import sco.carlukesoftware.jetris.ui.theme.TetrisBlueBorderColor
import sco.carlukesoftware.jetris.ui.theme.TetrisBlueColor
import sco.carlukesoftware.jetris.ui.theme.TetrisGreenBorderColor
import sco.carlukesoftware.jetris.ui.theme.TetrisGreenColor
import sco.carlukesoftware.jetris.ui.theme.TetrisOrangeBorderColor
import sco.carlukesoftware.jetris.ui.theme.TetrisOrangeColor
import sco.carlukesoftware.jetris.ui.theme.TetrisPinkBorderColor
import sco.carlukesoftware.jetris.ui.theme.TetrisPinkColor
import sco.carlukesoftware.jetris.ui.theme.TetrisPurpleBorderColor
import sco.carlukesoftware.jetris.ui.theme.TetrisPurpleColor
import sco.carlukesoftware.jetris.ui.theme.TetrisRedBorderColor
import sco.carlukesoftware.jetris.ui.theme.TetrisRedColor
import sco.carlukesoftware.jetris.ui.theme.TetrisYellowBorderColor
import sco.carlukesoftware.jetris.ui.theme.TetrisYellowColor

@Composable
fun BlockTitle(modifier: Modifier = Modifier) {
    val title = "JETRIS"
    val colors = arrayOf(
        Pair(TetrisRedColor, TetrisRedBorderColor),
        Pair(TetrisYellowColor, TetrisYellowBorderColor),
        Pair(TetrisBlueColor, TetrisBlueBorderColor),
        Pair(TetrisOrangeColor, TetrisOrangeBorderColor),
        Pair(TetrisPurpleColor, TetrisPurpleBorderColor),
        Pair(TetrisGreenColor, TetrisGreenBorderColor),
    )

    Row(
        modifier = modifier
            .padding(1.dp)
    ) {
        title.forEachIndexed { index, char ->
            CharBlock(
                size = 60.dp,
                blockColor = colors[index].first,
                borderColor = colors[index].second,
                char = char
            )
        }
    }
}

@Preview
@Composable
private fun BlockTitlePreview() {
    JetrisTheme {
        BlockTitle()
    }
}
