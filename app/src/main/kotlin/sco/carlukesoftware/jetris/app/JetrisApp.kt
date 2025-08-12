package sco.carlukesoftware.jetris.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import sco.carlukesoftware.jetris.di.gameModule

class JetrisApp : Application()  {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@JetrisApp)
            androidLogger()

            modules(
                gameModule
            )
        }
    }
}
