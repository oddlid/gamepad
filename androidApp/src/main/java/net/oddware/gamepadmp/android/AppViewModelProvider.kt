package net.oddware.gamepadmp.android

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            GameViewModel(application().container.gamesRepository)
        }

        initializer {
            PlayerViewModel(application().container.playerRepository)
        }
    }
}

fun CreationExtras.application(): App =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App)