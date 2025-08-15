package sco.carlukesoftware.jetris.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module

// Define named qualifiers if you need multiple dispatchers/scopes
object CoroutineScopes {
    const val IO_SCOPE = "ioScope"
    const val DEFAULT_SCOPE = "defaultScope"
    const val DATASTORE_SCOPE = "datastoreScope"
}

private object DispatcherQualifiers {
    const val IO_DISPATCHER = "ioDispatcher"
    const val DEFAULT_DISPATCHER = "defaultDispatcher"
    const val MAIN_DISPATCHER = "mainDispatcher"
}

val coroutineModule = module {

    // --- Option 1: Provide Dispatchers Separately (Recommended for flexibility) ---
    single<CoroutineDispatcher>(named(DispatcherQualifiers.IO_DISPATCHER)) {
        Dispatchers.IO
    }
    single<CoroutineDispatcher>(named(DispatcherQualifiers.DEFAULT_DISPATCHER)) {
        Dispatchers.Default
    }
    single<CoroutineDispatcher>(named(DispatcherQualifiers.MAIN_DISPATCHER)) {
        Dispatchers.Main // Or Dispatchers.Main.immediate
    }


    // --- Option 2: Provide Scopes directly with specific dispatchers ---

    // Provide a CoroutineScope with Dispatchers.IO
    // Using SupervisorJob so if one child coroutine fails, it doesn't cancel the whole scope
    single<CoroutineScope>(named(CoroutineScopes.IO_SCOPE)) {
        // Pass the IO dispatcher provided above
        CoroutineScope(SupervisorJob() + get<CoroutineDispatcher>(named(DispatcherQualifiers.IO_DISPATCHER)))
        // Alternatively, if you didn't provide the dispatcher separately:
        // CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

    // Provide a CoroutineScope with Dispatchers.Default
    single<CoroutineScope>(named(CoroutineScopes.DEFAULT_SCOPE)) {
        CoroutineScope(SupervisorJob() + get<CoroutineDispatcher>(named(DispatcherQualifiers.DEFAULT_DISPATCHER)))
    }

    // Provide a CoroutineScope specifically for DataStore if needed,
    // or reuse a general IO scope.
    single<CoroutineScope>(named(CoroutineScopes.DATASTORE_SCOPE)) {
        CoroutineScope(Dispatchers.IO + SupervisorJob())
    }

    // --- Option 3: Provide a scope that takes a dispatcher as a parameter ---
    // This is useful if the scope's dispatcher isn't fixed at definition time,
    // though less common for globally provided scopes.
    // factory<CoroutineScope> { params -> // params is ParametersHolder
    //     val dispatcher: CoroutineDispatcher = params.get() // Assuming dispatcher is passed
    //     CoroutineScope(SupervisorJob() + dispatcher)
    // }
}
