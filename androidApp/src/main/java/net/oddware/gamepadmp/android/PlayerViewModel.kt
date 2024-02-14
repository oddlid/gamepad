package net.oddware.gamepadmp.android

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel

class PlayerViewModel : ViewModel() {
    private val _players = getPlayers().toMutableStateList()
    val players: List<Player>
        get() = _players

    fun getSelectedPlayers() = _players.filter { it.selected }

    fun getSelectedPlayerIDs() = getSelectedPlayers().map { it.id }

    fun toggleSelection(index: Int) {
        val player = _players[index]
        _players[index] = player.copy(selected = !player.selected)
    }

    fun find(id: Int): Player? = _players.find { it.id == id }

    fun remove(player: Player) {
        _players.remove(player)
    }

    fun add(name: String) {
        _players.add(Player(getNextPlayerID(_players), name = name, selected = false))
    }
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