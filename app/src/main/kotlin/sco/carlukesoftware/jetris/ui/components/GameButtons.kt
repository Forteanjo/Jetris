package sco.carlukesoftware.jetris.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import sco.carlukesoftware.jetris.R
import sco.carlukesoftware.jetris.ui.theme.JetrisTheme

@Composable
fun GameButton(
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonSize: Dp = 64.dp // Make button size a parameter
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .size(buttonSize) // Use .size() for equal width and height
            .padding(4.dp),
        shape = MaterialTheme.shapes.extraSmall,
        contentPadding = PaddingValues(0.dp) // Set to 0.dp to remove all padding
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
        )
    }
}

@Composable
fun GameButtons(
    onMoveLeft: () -> Unit,
    onMoveRight: () -> Unit,
    onRotate: () -> Unit,
    onMoveDown: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        // Middle Row (Left, Down/Action, Right Buttons)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            GameButton(
                icon = painterResource(id = R.drawable.outline_arrow_left_24),
                onClick = onMoveLeft
            )
            // Center button (could be 'down' or an action button)
            GameButton(
                icon = painterResource(
                    id = R.drawable.outline_rotate_90_degrees_cw_24
                ),
                onClick = onRotate // Or a different action
            )
            GameButton(
                icon = painterResource(id = R.drawable.outline_arrow_right_24),
                onClick = onMoveRight
            )
        }

        // Top Row (Rotate Button)
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            // Spacer for left placeholder, GameButton for rotate, Spacer for right placeholder
            Spacer(
                Modifier
                    .size(BUTTON_WIDTH)
            ) // Placeholder for left

            GameButton(
                icon = painterResource(
                    id = R.drawable.outline_arrow_drop_down_24
                ),
                onClick = onMoveDown
            )
            Spacer(Modifier.size(BUTTON_WIDTH)) // Placeholder for right
        }
    }
}

val BUTTON_WIDTH = 64.dp

@Preview
@Composable
private fun GameButtonsPreview() {
    JetrisTheme {
        GameButtons(
            onMoveLeft = { },
            onMoveRight = { },
            onRotate = { },
            onMoveDown = { }
        )
    }
}
