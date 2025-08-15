package sco.carlukesoftware.jetris.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val USER_PREFERENCES_NAME = "user_preferences"

val preferencesModule = module {


    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            scope = get(named(CoroutineScopes.DATASTORE_SCOPE)), // Use the injected scope
            produceFile = {
                androidContext()
                    .preferencesDataStoreFile(USER_PREFERENCES_NAME)
            }
        )
    }
}
