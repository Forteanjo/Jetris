package sco.carlukesoftware.themeswitcher.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sco.carlukesoftware.themeswitcher.data.ThemeMode
import sco.carlukesoftware.themeswitcher.manager.ThemeManager

@Composable
fun ThemeSwitcher(
    themeManager: ThemeManager
) {
    val themeMode by themeManager.themeMode.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults
            .cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            ThemeOption(
                title = "Light",
                description = "Always use light theme",
                selected = themeMode is ThemeMode.Light,
                onClick = { themeManager.updateThemeMode(ThemeMode.Light) }
            )

            ThemeOption(
                title = "Dark",
                description = "Always use dark theme",
                selected = themeMode is ThemeMode.Dark,
                onClick = { themeManager.updateThemeMode(ThemeMode.Dark) }
            )

            ThemeOption(
                title = "Follow system",
                description = "Match your device settings",
                selected = themeMode is ThemeMode.System,
                onClick = { themeManager.updateThemeMode(ThemeMode.System) }
            )

            ThemeOption(
                title = "High contrast",
                description = "Optimized for accessibility",
                selected = themeMode is ThemeMode.HighContrast,
                onClick = { themeManager.updateThemeMode(ThemeMode.HighContrast) }
            )
        }
    }
}
