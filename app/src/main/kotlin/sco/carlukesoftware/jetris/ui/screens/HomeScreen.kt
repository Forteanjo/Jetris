package sco.carlukesoftware.jetris.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sco.carlukesoftware.jetris.ui.components.BlockTitle
import sco.carlukesoftware.jetris.ui.components.GameScreenGrid
import sco.carlukesoftware.jetris.ui.components.homeScreenGrid
import sco.carlukesoftware.jetris.ui.theme.JetrisTheme
import sco.carlukesoftware.jetris.ui.theme.TetrisGridBorderColor

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
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
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
        }

        Button(
            onClick = { /*TODO*/ },
            modifier = modifier
        ) {
            Text(
                text = "Play",
                modifier = modifier
                    .padding(8.dp),
                fontSize = 36.sp
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    JetrisTheme {
        HomeScreen()
    }
}
