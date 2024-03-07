package net.oddware.gamepadmp.android

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel

class GameRoundViewModel : ViewModel() {
    private val noGame = Game(-1, "UNDEFINED")
    var currentGame = noGame
        private set
    private var _activePlayers = listOf<ActivePlayer>().toMutableStateList()
    val activePlayers = _activePlayers


    fun startRound(game: Game, players: List<ActivePlayer>) {
        currentGame = game
        _activePlayers.apply {
            clear()
            addAll(players)
        }
    }

    fun stopRound() {
        currentGame = noGame
        _activePlayers.clear()
    }

    fun update() {
        _activePlayers.sortWith(compareByDescending { it.currentPoints })
    }
}