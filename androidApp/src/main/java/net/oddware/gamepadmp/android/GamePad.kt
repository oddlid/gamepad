package net.oddware.gamepadmp.android

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

enum class AppMode {
    LIST_GAMES,
    LIST_PLAYERS,
    PLAY,
}

@Composable
fun GamePad(
    modifier: Modifier = Modifier,
    playerViewModel: PlayerViewModel = viewModel(),
    gameViewModel: GameViewModel = viewModel(),
    gameRoundViewModel: GameRoundViewModel = viewModel(),
) {
    var mode by rememberSaveable { mutableStateOf(AppMode.LIST_GAMES) }

    when (mode) {
        AppMode.LIST_GAMES -> {
            GameListScreen(
                modifier = modifier,
                gameViewModel = gameViewModel,
                onSelect = { id ->
                    gameViewModel.currentID = id
                    mode = AppMode.LIST_PLAYERS
                },
            )
        }

        AppMode.LIST_PLAYERS -> {
            PlayerListScreen(
                modifier = modifier,
                playerViewModel = playerViewModel,
                onBack = {
                    mode = AppMode.LIST_GAMES
                },
                onPlay = {
                    gameViewModel.find(gameViewModel.currentID)?.also {
                        gameRoundViewModel.currentGame = it
                        gameRoundViewModel.setActivePlayers(playerViewModel.getActivePlayers())
                        mode = AppMode.PLAY
                    }
                },
            )
        }

        AppMode.PLAY -> {
            GameRoundScreen(
                modifier = modifier,
                gameRoundViewModel = gameRoundViewModel,
                onBack = {
                    mode = AppMode.LIST_PLAYERS
                }
            )
        }
    }
}