package sco.carlukesoftware.jetris.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sco.carlukesoftware.jetris.data.BlockColors
import sco.carlukesoftware.jetris.ui.theme.JetrisTheme
import sco.carlukesoftware.jetris.ui.theme.TetrisBlueBorderColor
import sco.carlukesoftware.jetris.ui.theme.TetrisBlueColor
import sco.carlukesoftware.jetris.ui.theme.TetrisGreenBorderColor
import sco.carlukesoftware.jetris.ui.theme.TetrisGreenColor
import sco.carlukesoftware.jetris.ui.theme.TetrisOrangeBorderColor
import sco.carlukesoftware.jetris.ui.theme.TetrisOrangeColor
import sco.carlukesoftware.jetris.ui.theme.TetrisPurpleBorderColor
import sco.carlukesoftware.jetris.ui.theme.TetrisPurpleColor
import sco.carlukesoftware.jetris.ui.theme.TetrisRedBorderColor
import sco.carlukesoftware.jetris.ui.theme.TetrisRedColor
import sco.carlukesoftware.jetris.ui.theme.TetrisYellowBorderColor
import sco.carlukesoftware.jetris.ui.theme.TetrisYellowColor
import sco.carlukesoftware.jetris.utils.GRID_COLUMNS
import sco.carlukesoftware.jetris.utils.GRID_SPACING
import sco.carlukesoftware.jetris.utils.MAX_BLOCK_SIZE
import sco.carlukesoftware.jetris.utils.NEXT_BLOCK_GRID_COLUMNS
import sco.carlukesoftware.jetris.utils.SCREEN_PADDING

@Composable
fun BlockTitle(
    modifier: Modifier = Modifier
) {
    val title = "JETRIS"
    val blockPadding = 2.dp
    val colors = arrayOf(
        BlockColors(TetrisRedColor, TetrisRedBorderColor),
        BlockColors(TetrisYellowColor, TetrisYellowBorderColor),
        BlockColors(TetrisBlueColor, TetrisBlueBorderColor),
        BlockColors(TetrisOrangeColor, TetrisOrangeBorderColor),
        BlockColors(TetrisPurpleColor, TetrisPurpleBorderColor),
        BlockColors(TetrisGreenColor, TetrisGreenBorderColor),
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(SCREEN_PADDING),
        contentAlignment = Alignment.Center
    ) {
        // maxWidth and maxHeight are the available space AFTER screen padding
        val availableWidth = this.maxWidth
        val availableHeight = this.maxHeight // Full height for the content area


        // 1. Calculate how much width is needed for all horizontal elements
        // Main grid columns + Next piece grid columns + spacing between them
        val totalHorizontalBlocks = title.length
        val blockSize = minOf(availableWidth / totalHorizontalBlocks, MAX_BLOCK_SIZE)

        Row(
            modifier = Modifier
                .padding(1.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            title.forEachIndexed { index, char ->
                CharBlock(
                    size = blockSize,
                    blockColor = colors[index].blockColor,
                    borderColor = colors[index].borderColor,
                    char = char
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BlockTitlePreview() {
    JetrisTheme {
        BlockTitle()
    }
}
