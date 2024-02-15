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
        get() = _players.sortedBy { it.id }

    var currentID: Int = -1
    val currentMode: MutableState<Mode> = mutableStateOf(Mode.LIST)

    fun getSelectedPlayers() = _players.filter { it.selected }

    fun getSelectedPlayerIDs() = getSelectedPlayers().map { it.id }

    fun hasSelection() = getSelectedPlayers().isNotEmpty()

    fun allSelected() = _players.size == getSelectedPlayers().size


    fun toggleSelection(player: Player) {
        _players.find { it.id == player.id }?.let {
            _players.remove(it)
            _players.add(it.copy(selected = !it.selected))
        }
    }

    fun selectAll(selected: Boolean) = _players.replaceAll { it.copy(selected = selected) }

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