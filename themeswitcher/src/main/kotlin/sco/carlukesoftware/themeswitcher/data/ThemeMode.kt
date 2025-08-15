package sco.carlukesoftware.themeswitcher.data

sealed class ThemeMode {
    data object Light : ThemeMode()
    data object Dark : ThemeMode()
    data object System : ThemeMode()
    data object HighContrast : ThemeMode()

    fun toStorageString(): String = when (this) {
        Light -> "light"
        Dark -> "dark"
        System -> "system"
        HighContrast -> "high_contrast"
    }

    companion object {
        fun fromStorageString(value: String?): ThemeMode = when (value) {
            "light" -> Light
            "dark" -> Dark
            "high_contrast" -> HighContrast
            else -> System // Default to System
        }
    }
}
