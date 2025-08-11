package sco.carlukesoftware.jetris.ui.screens

import android.R.attr.top
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sco.carlukesoftware.jetris.ui.components.BlockTitle
import sco.carlukesoftware.jetris.ui.theme.JetrisTheme

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
        BlockTitle(
            modifier = modifier
                .padding(top = 64.dp)
        )

        Button(
            onClick = { /*TODO*/ },
            modifier = modifier
                .padding(top = 64.dp)
        ) {
            Text(
                text = "Play",
                modifier = modifier
                    .padding(16.dp),
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
