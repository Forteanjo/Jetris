package sco.carlukesoftware.jetris.ui.screens

import android.text.SpannedString
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import sco.carlukesoftware.jetris.ui.components.BlockTitle
import sco.carlukesoftware.jetris.ui.components.GameButtons
import sco.carlukesoftware.jetris.ui.components.GameScreenGrid
import sco.carlukesoftware.jetris.ui.components.homeScreenGrid
import sco.carlukesoftware.jetris.ui.theme.JetrisTheme
import sco.carlukesoftware.jetris.utils.emptyGameGrid
import sco.carlukesoftware.jetris.utils.emptyNextBlockGrid
import sco.carlukesoftware.jetris.viewmodel.GameViewModel

@Composable
fun GameScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    gameViewModel: GameViewModel = koinInject()
) {
    val gameState = gameViewModel.gameState.collectAsState()
    val nextBlockGrid = gameViewModel.nextBlockGrid.collectAsState()

    val windowInfo = LocalWindowInfo.current
    val containerSize = windowInfo.containerSize
    val blockSize = (containerSize.width / (gameState.value.grid.size + nextBlockGrid.value.size)) * 0.6

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment
            .CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.2f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BlockTitle(
                modifier = modifier
                    .padding(top = 12.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.6f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top,
        ) {
            GameScreenGrid(
                gameGrid = gameState.value.grid,
                blockSize = blockSize.dp,
                modifier = Modifier
                    .padding(8.dp)
            )

            Spacer(
                modifier = Modifier
                    .width(16.dp)
            )

            GameScreenGrid(
                gameGrid = nextBlockGrid.value,
                blockSize = blockSize.dp,
                modifier = Modifier
                    .padding(8.dp)
            )

        }

        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
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

@Preview
@Composable
private fun GameScreenPreview() {
    JetrisTheme {
        GameScreen(
            onBackClick = { }
        )
    }
}
