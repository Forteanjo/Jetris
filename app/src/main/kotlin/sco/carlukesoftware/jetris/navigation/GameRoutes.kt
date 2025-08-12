package sco.carlukesoftware.jetris.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface GameRoutes : NavKey {

    @Serializable
    data object HomeScreen : GameRoutes

    @Serializable
    data object GameScreen : GameRoutes

    @Serializable
    data object GameOverScreen : GameRoutes

    @Serializable
    data object SettingsScreen : GameRoutes

}
