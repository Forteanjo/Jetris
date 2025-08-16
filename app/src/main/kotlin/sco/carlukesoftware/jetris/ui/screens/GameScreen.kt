package sco.carlukesoftware.jetris.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import sco.carlukesoftware.jetris.ui.components.BlockTitle
import sco.carlukesoftware.jetris.ui.components.GameButtons
import sco.carlukesoftware.jetris.ui.components.GameScreenGrid
import sco.carlukesoftware.jetris.ui.theme.JetrisTheme
import sco.carlukesoftware.jetris.utils.GRID_COLUMNS
import sco.carlukesoftware.jetris.utils.GRID_ROWS
import sco.carlukesoftware.jetris.utils.GRID_SPACING
import sco.carlukesoftware.jetris.utils.NEXT_BLOCK_GRID_COLUMNS
import sco.carlukesoftware.jetris.utils.SCREEN_PADDING
import sco.carlukesoftware.jetris.viewmodel.GameViewModel

@Composable
fun GameScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    gameViewModel: GameViewModel = koinInject()
) {
    val gameState = gameViewModel.gameState.collectAsState()
    val nextBlockGrid = gameViewModel.nextBlockGrid.collectAsState()

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .padding(SCREEN_PADDING)
    ) {
        // maxWidth and maxHeight are the available space AFTER screen padding
        val availableWidth = this.maxWidth
        val availableHeight = this.maxHeight // Full height for the content area


        // --- Calculate BlockSize ---

        // 1. Calculate how much width is needed for all horizontal elements
        // Main grid columns + Next piece grid columns + spacing between them
        val totalHorizontalBlocks = GRID_COLUMNS + NEXT_BLOCK_GRID_COLUMNS
        val widthRequiredForGridsOnly = availableWidth - GRID_SPACING // Subtract fixed spacing first

        // Calculate block size based on width, considering both grids
        val blockSizeBasedOnWidth = if (totalHorizontalBlocks > 0) {
            widthRequiredForGridsOnly / totalHorizontalBlocks
        } else {
            availableWidth // Fallback if no blocks, take full width (shouldn't happen)
        }

        // 2. Calculate block size based on height (primarily for the main game grid)
        // Let's assume the game area (main grid + title) takes up a certain percentage of height,
        // and controls take the rest. Or, dedicate most height to the main grid.
        val topAreaMaxHeight = availableHeight * 0.75f // e.g., Title + Grids take 75% height
        // Adjust this factor based on your layout needs
        val blockSizeBasedOnHeight = if (GRID_ROWS > 0) {
            topAreaMaxHeight / GRID_ROWS
        } else {
            topAreaMaxHeight // Fallback
        }

        // 3. The actual blockSize must be the minimum of these to fit both dimensions
        val blockSize = minOf(blockSizeBasedOnWidth, blockSizeBasedOnHeight)

        // Ensure blockSize is not zero or negative if calculations are off or space is too small
        val finalBlockSize = if (blockSize > 0.dp) blockSize else 1.dp // Minimum 1.dp to avoid issues

        // --- Layout the UI ---
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment
                .CenterHorizontally
        ) {
            BlockTitle(
                modifier = Modifier
                    .padding(
                        top = 12.dp,
                        bottom = 16.dp
                    ) // Add some bottom padding too
            )


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    // .height(IntrinsicSize.Min) // Optional: If you want the row to wrap content height based on calculated sizes
                    .weight(0.75f), // Give more weight to the game area
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top,
            ) {
                GameScreenGrid(
                    gameGrid = gameState.value.grid,
                    blockSize = blockSize,
                    modifier = Modifier
                        .padding(GRID_SPACING)
                )

                // Only show next block grid if there's enough space and it's not empty
                if (finalBlockSize * NEXT_BLOCK_GRID_COLUMNS < (availableWidth - (finalBlockSize * GRID_COLUMNS) - GRID_SPACING)) {
                    GameScreenGrid(
                        gameGrid = nextBlockGrid.value, // Use the collected state
                        blockSize = finalBlockSize,
                        modifier = Modifier
                            .padding(start = GRID_SPACING / 2) // Half spacing on the other side
                    )
                } else {
                    // Optionally provide a smaller placeholder or hide if not enough space
                    Spacer(
                        modifier = Modifier
                            .width(finalBlockSize * NEXT_BLOCK_GRID_COLUMNS)
                    )
                }
            }

            // Game Controls
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.25f), // Remaining space for controls
                contentAlignment = Alignment.Center
            ) {
                GameButtons(
                    onMoveLeft = { gameViewModel.onMoveLeft() },
                    onMoveRight = { gameViewModel.onMoveRight() },
                    onRotate = { gameViewModel.onRotate() },
                    onMoveDown = { gameViewModel.onHardDrop() },
                )
            }
        }

    }
}

@Preview
@Composable
private fun GameScreenPreview() {
    JetrisTheme {
        GameScreen(
            onBackClick = { }
        )
    }
}
