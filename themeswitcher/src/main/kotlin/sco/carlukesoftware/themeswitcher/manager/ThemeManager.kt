package sco.carlukesoftware.themeswitcher.manager

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import sco.carlukesoftware.themeswitcher.data.AccentColor
import sco.carlukesoftware.themeswitcher.data.AppTheme
import sco.carlukesoftware.themeswitcher.data.ThemeMode

/**
 * Manages the application's theme, allowing users to switch between light, dark, system-default,
 * and high-contrast themes.
 *
 * This class observes changes in user-selected theme mode and the system's dark mode status
 * to determine the appropriate [AppTheme] to apply. Theme preferences are persisted using
 * Jetpack DataStore.
 *
 * To correctly observe system dark mode changes, the [ObserveSystemDarkMode] composable function
 * must be called within a Composable context (e.g., in your main App composable).
 *
 * @property dataStore The [DataStore] instance used for persisting theme preferences.
 * @property externalScope A [CoroutineScope] used for performing DataStore operations,
 *                         typically on an IO dispatcher to avoid blocking the main thread.
 *                         Defaults to a new scope with `Dispatchers.IO`.
 */
class ThemeManager(
    private val dataStore: DataStore<Preferences>,
    private val externalScope: CoroutineScope = CoroutineScope(Dispatchers.IO) // For DataStore operations
) {
    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val ACCENT_COLOR = stringPreferencesKey("accent_color")
    }

    private val _currentTheme = MutableStateFlow(AppTheme(isDarkMode = false))
    val currentTheme: StateFlow<AppTheme> = _currentTheme.asStateFlow()

    private val _themeMode = MutableStateFlow<ThemeMode>(ThemeMode.System)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    // Variable to hold the system's dark mode state, updated by a Composable
    private val _isSystemInDarkTheme = MutableStateFlow(false)


    init {
        // Load initial theme mode from DataStore
        externalScope.launch {
            dataStore.data
                .map { preferences ->
                    ThemeMode.fromStorageString(preferences[PreferencesKeys.THEME_MODE])
                }
                .distinctUntilChanged()
                .collectLatest { loadedMode ->
                    _themeMode.value = loadedMode
                    // Initial theme update needs current system dark status.
                    // This will be triggered by observeSystemDarkModeChanges or manually if not in Composable scope.
                    // For simplicity here, we'll rely on the Composable observing this.
                    // If ThemeManager is used outside Composable, you'd need another way to get initial system dark state.
                }
        }

        // Combine themeMode and isSystemInDarkTheme to update the actual theme
        externalScope.launch {
            combine(_themeMode, _isSystemInDarkTheme) { mode, isSystemDark ->
                Pair(mode, isSystemDark)
            }
                .distinctUntilChanged()
                .collectLatest { (mode, isSystemDark) ->
                    updateActualThemeInternal(mode, isSystemDark)
                }
        }
    }

    fun updateAccentColor(accentColor: AccentColor) {
        _currentTheme.value = _currentTheme.value.copy(
            accentColor = accentColor.color
        )
        saveAccentColor(accentColor)
    }

    fun saveAccentColor(accentColor: AccentColor) {
        externalScope.launch {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.ACCENT_COLOR] = accentColor.name
            }
        }
    }

    /**
     * Updates the application's theme mode.
     *
     * This function performs an optimistic update to the UI by immediately setting the `_themeMode` state.
     * This makes the theme mode selection (e.g., in a settings UI) feel responsive.
     *
     * The actual application theme (dark/light/high contrast) is then updated when the `combine`
     * collector in the `init` block observes this change to `_themeMode`.
     *
     * After updating the internal state, it persists the selected `mode` to DataStore.
     * The persisted value will be loaded if the app restarts or if `ThemeManager` is re-initialized.
     *
     * @param mode The [ThemeMode] to set for the application (Light, Dark, System, HighContrast).
     */
    fun updateThemeMode(mode: ThemeMode) {
        // Optimistic update for UI responsiveness
        // The collect block in init will eventually set _themeMode.value
        // but this makes the UI update slightly faster for the themeMode itself.
        // However, the actual theme (dark/light) update relies on the collect block.
        // For directness, we can also call saveThemeMode and let the flow trigger the update.
        _themeMode.value = mode // This will trigger the combine collector
        saveThemeMode(mode)
    }

    private fun saveThemeMode(mode: ThemeMode) {
        externalScope.launch {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.THEME_MODE] = mode.toStorageString()
            }
        }
    }




    /**
     * Observes the system's dark mode setting and updates the internal state.
     * This function is a Composable and should be called from within a Composable context,
     * typically at the root of your application, to ensure the ThemeManager is aware of
     * system theme changes.
     *
     * It uses `isSystemInDarkTheme()` to get the current system theme and `LaunchedEffect`
     * to react to changes in this value, updating `_isSystemInDarkTheme` accordingly.
     * This, in turn, allows the `ThemeManager` to adjust the `currentTheme` based on the
     * selected `themeMode`.
     */// This function should be called from a Composable context
    @Composable
    fun ObserveSystemDarkMode() {
        val systemDark = isSystemInDarkTheme()
        // Use LaunchedEffect to update the internal state when system theme changes
        LaunchedEffect(systemDark) {
            _isSystemInDarkTheme.value = systemDark
        }
    }

    // Internal function, not @Composable
    private fun updateActualThemeInternal(mode: ThemeMode, isSystemDark: Boolean) {
        val shouldUseDark: Boolean
        val isHighContrastMode = mode is ThemeMode.HighContrast

        when (mode) {
            is ThemeMode.Light -> {
                shouldUseDark = false
            }
            is ThemeMode.Dark -> {
                shouldUseDark = true
            }
            is ThemeMode.System -> {
                shouldUseDark = isSystemDark
            }
            is ThemeMode.HighContrast -> {
                // Assuming HighContrast implies dark, or follows system for dark/light
                // If HighContrast has its own light/dark, this logic needs adjustment.
                // For this example, let's say HighContrast is always dark.
                shouldUseDark = true
            }
        }

        _currentTheme.value = AppTheme( // Create a new instance
            isDarkMode = shouldUseDark,
            isHighContrast = isHighContrastMode
        )
    }
}
