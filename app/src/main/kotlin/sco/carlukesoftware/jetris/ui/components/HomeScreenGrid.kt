package sco.carlukesoftware.jetris.ui.components

import android.net.wifi.hotspot2.pps.HomeSp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sco.carlukesoftware.jetris.ui.data.BlockColor
import sco.carlukesoftware.jetris.ui.theme.JetrisTheme
import sco.carlukesoftware.jetris.utils.toBlockColors

@Composable
fun HomeScreenGrid(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(1.dp)
    ) {
        homeScreenGrid.forEachIndexed { rowIndex, row ->
            Row {
                row.forEach { block ->
                    val blockColors = block.toBlockColors()
                    Block(
                        size = 30.dp,
                        borderWidth = 4.dp,
                        blockColor = blockColors.blockColor,
                        borderColor = blockColors.borderColor
                    )

                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenGridPreview() {
    JetrisTheme {
        HomeScreenGrid()
    }
}

private val homeScreenGrid =
    arrayOf(
        arrayOf(
            BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY,
            BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY
        ),
        arrayOf(
            BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY,
            BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY
        ),
        arrayOf(
            BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY,
            BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY
        ),
        arrayOf(
            BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY,
            BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY
        ),
        arrayOf(
            BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY,
            BlockColor.EMPTY, BlockColor.RED, BlockColor.RED, BlockColor.EMPTY, BlockColor.EMPTY
        ),
        arrayOf(
            BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY,
            BlockColor.RED, BlockColor.RED, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY
        ),
        arrayOf(
            BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY,
            BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY
        ),
        arrayOf(
            BlockColor.EMPTY, BlockColor.RED, BlockColor.RED, BlockColor.EMPTY, BlockColor.EMPTY,
            BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY
        ),
        arrayOf(
            BlockColor.GREEN, BlockColor.RED, BlockColor.RED, BlockColor.EMPTY, BlockColor.EMPTY,
            BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY
        ),
        arrayOf(
            BlockColor.GREEN, BlockColor.BLUE, BlockColor.BLUE, BlockColor.PINK, BlockColor.PINK,
            BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY, BlockColor.EMPTY
        ),
        arrayOf(
            BlockColor.GREEN, BlockColor.BLUE, BlockColor.BLUE, BlockColor.YELLOW, BlockColor.GREEN,
            BlockColor.GREEN, BlockColor.GREEN, BlockColor.EMPTY, BlockColor.BLUE, BlockColor.BLUE
        ),
        arrayOf(
            BlockColor.GREEN, BlockColor.YELLOW, BlockColor.YELLOW, BlockColor.YELLOW, BlockColor.PURPLE,
            BlockColor.PURPLE, BlockColor.RED, BlockColor.ORANGE, BlockColor.ORANGE, BlockColor.ORANGE
        ),
    )
