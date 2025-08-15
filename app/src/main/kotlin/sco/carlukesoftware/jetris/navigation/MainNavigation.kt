package sco.carlukesoftware.jetris.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import org.koin.compose.koinInject
import sco.carlukesoftware.jetris.ui.screens.GameScreen
import sco.carlukesoftware.jetris.ui.screens.HomeScreen
import sco.carlukesoftware.themeswitcher.data.ThemeMode
import sco.carlukesoftware.themeswitcher.manager.ThemeManager
import sco.carlukesoftware.themeswitcher.utils.highContrastColorScheme

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    themeManager: ThemeManager = koinInject()
) {
    val backStack = rememberNavBackStack(
        GameRoutes.HomeScreen
    )

    // This will observe system dark mode changes and update ThemeManager
    themeManager.ObserveSystemDarkMode()

    val appTheme by themeManager.currentTheme.collectAsState()
    val themeMode by themeManager.themeMode.collectAsState()

    // This is the secret sauce - listening to system changes
    val systemDarkMode = isSystemInDarkTheme()

    LaunchedEffect(systemDarkMode, themeMode) {
        if (themeMode == ThemeMode.System) {
            themeManager.handleSystemThemeChange(systemDarkMode)
        }
    }

    val colors = if (appTheme.isHighContrast) {
        highContrastColorScheme(appTheme.isDarkMode)
    } else if (appTheme.isDarkMode) {
        darkColorScheme(primary = appTheme.accentColor)
    } else {
        lightColorScheme(primary = appTheme.accentColor)
    }

    val defaultModifier = Modifier
        .padding(WindowInsets.systemBars.asPaddingValues())

    MaterialTheme(
        colorScheme = colors,
    ) {
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


}
