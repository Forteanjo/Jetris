package sco.carlukesoftware.jetris.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import sco.carlukesoftware.jetris.di.coroutineModule
import sco.carlukesoftware.jetris.di.gameModule
import sco.carlukesoftware.jetris.di.preferencesModule
import sco.carlukesoftware.jetris.di.themeManagerModule

class JetrisApp : Application()  {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@JetrisApp)
            androidLogger()

            modules(
                coroutineModule,
                preferencesModule,
                themeManagerModule,
                gameModule
            )
        }
    }
}
