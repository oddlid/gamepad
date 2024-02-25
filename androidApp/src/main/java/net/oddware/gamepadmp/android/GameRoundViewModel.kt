package net.oddware.gamepadmp.android

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel

class GameRoundViewModel : ViewModel() {
    val noGame = Game(-1, "UNDEFINED")
    var currentGame = noGame
    private var _activePlayers = listOf<ActivePlayer>().toMutableStateList()
    val activePlayers = _activePlayers

    fun setActivePlayers(players: List<ActivePlayer>) {
        _activePlayers.apply {
            clear()
            addAll(players)
        }
    }

    fun update() {
        _activePlayers.sortWith(compareByDescending { it.currentPoints })
    }
}