package sco.carlukesoftware.jetris.di

import androidx.appcompat.view.ActionBarPolicy
import org.koin.core.qualifier.named
import org.koin.dsl.module
import sco.carlukesoftware.themeswitcher.manager.ThemeManager

val themeManagerModule = module {
    single {
        ThemeManager(
            dataStore = get(), // Koin will inject the DataStore<Preferences> provided by dataModule
            externalScope = get(named(CoroutineScopes.IO_SCOPE)) // Koin injects the named IO_SCOPE
            // Or, if you want to use ThemeManager's default scope:
            // Just omit the externalScope parameter:
            // ThemeManager(dataStore = get())
        )
    }
}
