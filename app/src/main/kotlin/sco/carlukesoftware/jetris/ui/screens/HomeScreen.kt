package sco.carlukesoftware.jetris.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.serialization.json.Json.Default.configuration
import sco.carlukesoftware.jetris.ui.components.BlockTitle
import sco.carlukesoftware.jetris.ui.components.GameScreenGrid
import sco.carlukesoftware.jetris.ui.components.homeScreenGrid
import sco.carlukesoftware.jetris.ui.theme.JetrisTheme
import sco.carlukesoftware.jetris.ui.theme.TetrisGridBorderColor
import sco.carlukesoftware.jetris.utils.GRID_COLUMNS
import sco.carlukesoftware.jetris.utils.GRID_SPACING
import sco.carlukesoftware.jetris.utils.NEXT_BLOCK_GRID_COLUMNS
import sco.carlukesoftware.jetris.utils.SCREEN_PADDING

@Composable
fun HomeScreen(
    onPlayGameClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .padding(SCREEN_PADDING),
        contentAlignment = Alignment.Center
    ) {
        // maxWidth and maxHeight are the available space AFTER screen padding
        val availableWidth = this.maxWidth
        val availableHeight = this.maxHeight // Full height for the content area

        // 1. Calculate how much width is needed for all horizontal elements
        // Main grid columns + Next piece grid columns + spacing between them
        val totalHorizontalBlocks = homeScreenGrid.size
        val widthRequiredForGridsOnly = availableWidth // Subtract fixed spacing first

    }
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment
            .CenterHorizontally
    ) {
        val windowInfo = LocalWindowInfo.current
        val containerSize = windowInfo.containerSize
        val blockSize = (containerSize.width / homeScreenGrid.size) * 0.6

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.2f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BlockTitle(
                modifier = modifier
                    .padding(top = 64.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.8f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier
                    .background(
                        color = Color.White
                    )
                    .border(
                        width = 4.dp,
                        color = TetrisGridBorderColor,
                        shape = MaterialTheme.shapes.extraSmall
                    ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GameScreenGrid(
                    gameGrid = homeScreenGrid,
                    blockSize = blockSize.dp,
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
        }

        Button(
            onClick = onPlayGameClick,
            modifier = Modifier
                .width(200.dp)
                .height(64.dp)
        ) {
            Text(
                text = "Play",
                modifier = Modifier,
                fontSize = 36.sp
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    JetrisTheme {
        HomeScreen(
            onPlayGameClick = { }
        )
    }
}
