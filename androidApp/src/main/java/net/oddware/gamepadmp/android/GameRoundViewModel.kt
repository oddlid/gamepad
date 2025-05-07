package net.oddware.gamepadmp.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private val NO_GAME = Game(id = -1, name = "UNDEFINED")

data class GameRoundUIState(
    val game: Game = NO_GAME,
    val players: List<ActivePlayer> = emptyList(),
)

class GameRoundViewModel : ViewModel() {
    private var _uiState = MutableStateFlow(GameRoundUIState())
    val uiState = _uiState.asStateFlow()


    fun startRound(game: Game, players: List<ActivePlayer>) {
        viewModelScope.launch {
            _uiState.emit(GameRoundUIState(game, players))
        }
    }

    fun stopRound() {
        viewModelScope.launch {
            _uiState.emit(GameRoundUIState())
        }
    }

    fun update() {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(players = current.players.sortedWith(compareByDescending { it.currentPoints }))
            }
        }
    }
}