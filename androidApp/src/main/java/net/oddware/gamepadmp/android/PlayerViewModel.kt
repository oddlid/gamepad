package net.oddware.gamepadmp.android

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class PlayerViewModel(private val playerRepository: PlayerRepository) : ViewModel() {
    enum class Mode {
        LIST,
        ADD,
        EDIT,
    }

    val players = playerRepository.getAllPlayersStream()

    var currentID: Int = -1
    val currentMode: MutableState<Mode> = mutableStateOf(Mode.LIST)

//    private fun getSelectedPlayers() = players.map { list ->
//        list.filter { player ->
//            player.selected
//        }
//    }

    //    fun getActivePlayers() = getSelectedPlayers().map { ActivePlayer(it) }
//    fun getActivePlayers(): List<ActivePlayer> {
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                return getSelectedPlayers().first().map { ActivePlayer(it) }
//            }
//        }
//    }

//    fun getSelectedPlayerIDs() = getSelectedPlayers().map { it.id }

    fun hasSelection() = playerRepository.hasSelection()

    fun allSelected() = playerRepository.allSelected()


    suspend fun toggleSelection(id: Int) = playerRepository.toggleSelection(id)

    suspend fun selectAll(selected: Boolean) = playerRepository.selectAll(selected)

    fun find(id: Int) = playerRepository.getPlayerStream(id)

    suspend fun remove(player: Player) = playerRepository.deletePlayer(player)

    suspend fun add(name: String) {
        playerRepository.insertPlayer(Player(name = name))
    }

    suspend fun setName(player: Player, name: String) {
//        _players.find { it.id == player.id }?.let {
//            _players.remove(it)
//            _players.add(it.copy(name = name))
//        }
        playerRepository.updatePlayer(player.copy(name = name))
    }
}

//private fun getPlayers() = List(3) { i -> Player(i, "Player $i", false) }
//
//private fun getNextPlayerID(playerList: List<Player>): Int {
//    var nextID: Int = -1
//    playerList.forEach {
//        if (it.id > nextID) {
//            nextID = it.id
//        }
//    }
//    return nextID + 1
//}