package net.oddware.gamepadmp.android

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel

class PlayerViewModel : ViewModel() {
    enum class Mode {
        LIST,
        ADD,
        EDIT,
    }

    private val _players = getPlayers().toMutableStateList()
    val players: List<Player>
        get() = _players

    var currentID: Int = -1
    val currentMode: MutableState<Mode> = mutableStateOf(Mode.LIST)

    fun getSelectedPlayers() = _players.filter { it.selected }

    fun getSelectedPlayerIDs() = getSelectedPlayers().map { it.id }

    fun toggleSelection(index: Int) {
        val player = _players[index]
        _players[index] = player.copy(selected = !player.selected)
    }

    fun clearSelection() = _players.replaceAll { it.copy(selected = false) }

    fun selectAll() = _players.replaceAll { it.copy(selected = true) }

    fun find(id: Int): Player? = _players.find { it.id == id }

    fun remove(player: Player) {
        _players.remove(player)
    }

    fun add(name: String) {
        _players.add(Player(getNextPlayerID(_players), name = name, selected = false))
    }

    fun setName(player: Player, name: String) {
        _players.find { it.id == player.id }?.let {
            _players.remove(it)
            _players.add(it.copy(name = name))
        }
    }
}

private fun getPlayers() = List(3) { i -> Player(i, "Player $i", false) }

private fun getNextPlayerID(playerList: List<Player>): Int {
    var nextID: Int = -1
    playerList.forEach {
        if (it.id > nextID) {
            nextID = it.id
        }
    }
    return nextID + 1
}