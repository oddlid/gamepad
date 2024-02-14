package net.oddware.gamepadmp.android

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel

class PlayerViewModel : ViewModel() {
    private val _players = getPlayers().toMutableStateList()
    val players: List<Player>
        get() = _players
}

private fun getPlayers() = listOf<Player>()

private fun getNextPlayerID(playerList: List<Player>): Int {
    var nextID: Int = -1
    for (player in playerList) {
        if (player.id > nextID) {
            nextID = player.id
        }
    }
    return nextID + 1
}