package sco.carlukesoftware.jetris.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import sco.carlukesoftware.jetris.ui.screens.GameScreen
import sco.carlukesoftware.jetris.ui.screens.HomeScreen

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(
        GameRoutes.HomeScreen
    )

    val defaultModifier = Modifier
        .padding(WindowInsets.systemBars.asPaddingValues())

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
    ) { route ->
        when (route) {
            is GameRoutes.HomeScreen -> NavEntry(route) {
                HomeScreen(
                    onPlayGameClick =  {
                        backStack.add(
                            GameRoutes.GameScreen
                        )
                    },
                    modifier = defaultModifier
                )
            }

            is GameRoutes.GameScreen -> NavEntry(route) {
                GameScreen(
                    onBackClick = {
                        backStack.removeLastOrNull()
                    },
                    modifier = defaultModifier
                )

            }

            else -> NavEntry(route) {

            }
        }
    }
}
