package sco.carlukesoftware.themeswitcher.data

import androidx.compose.ui.graphics.Color

data class AppTheme(
    val isDarkMode: Boolean,
    val isHighContrast: Boolean = false,
    val accentColor: Color = Color.Blue
)
