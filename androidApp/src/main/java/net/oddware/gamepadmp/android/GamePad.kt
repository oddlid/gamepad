package net.oddware.gamepadmp.android

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

enum class AppMode {
    LIST_GAMES,
    LIST_PLAYERS,
    PLAY,
}

@Composable
fun GamePad(
    modifier: Modifier = Modifier,
    playerViewModel: PlayerViewModel = viewModel(factory = AppViewModelProvider.Factory),
    gameViewModel: GameViewModel = viewModel(factory = AppViewModelProvider.Factory),
    gameRoundViewModel: GameRoundViewModel = viewModel(),
) {
    var mode by rememberSaveable { mutableStateOf(AppMode.LIST_GAMES) }
    val gameUIState: GameScreenUIState by gameViewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    when (mode) {
        AppMode.LIST_GAMES -> {
            GameListScreen(
                modifier = modifier,
                gameViewModel = gameViewModel,
                onSelect = { game ->
                    gameViewModel.onSelect(game)
                    mode = AppMode.LIST_PLAYERS
                },
            )
        }

        AppMode.LIST_PLAYERS -> {
            PlayerListScreen(
                modifier = modifier,
                playerViewModel = playerViewModel,
                onBack = {
                    playerViewModel.onSelectAll(false)
                    mode = AppMode.LIST_GAMES
                },
                onPlay = {
                    scope.launch {
                        gameRoundViewModel.startRound(
                            gameUIState.currentGame,
                            playerViewModel.getActivePlayers(),
                        )
                    }
                    mode = AppMode.PLAY
                },
            )
        }

        AppMode.PLAY -> {
            GameRoundScreen(
                modifier = modifier,
                gameRoundViewModel = gameRoundViewModel,
                onBack = {
                    gameRoundViewModel.stopRound()
                    mode = AppMode.LIST_PLAYERS
                }
            )
        }
    }
}