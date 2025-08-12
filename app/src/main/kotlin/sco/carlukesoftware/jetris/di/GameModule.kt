package sco.carlukesoftware.jetris.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import sco.carlukesoftware.jetris.viewmodel.GameViewModel

val gameModule = module {

    viewModel { GameViewModel() }

}
