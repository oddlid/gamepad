package net.oddware.gamepadmp.android

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class GameViewModel(private val gamesRepository: GamesRepository) : ViewModel() {
    enum class Mode {
        LIST,
        ADD,
        EDIT,
    }

    private val _games = gamesRepository.getAllGamesStream()
    val games = _games

    var currentID: Int = -1
    val currentMode: MutableState<Mode> = mutableStateOf(Mode.LIST)

    fun find(id: Int) = gamesRepository.getGameStream(id)

    suspend fun remove(game: Game) {
//        _games.remove(game)
        gamesRepository.deleteGame(game)
    }

    suspend fun add(name: String) {
//        _games.add(Game(getNextGameID(_games), name))
        gamesRepository.insertGame(Game(name = name))
    }

    suspend fun setName(game: Game, name: String) {
//        _games.find { it.id == game.id }?.let {
//            _games.remove(it)
//            _games.add(it.copy(name = name))
//        }
        gamesRepository.updateGame(game.copy(name = name))
    }
}

//private fun getGames() = List(3) { i -> Game(i, "Game # $i") }
//
//private fun getNextGameID(gameList: List<Game>): Int {
//    var nextID: Int = -1
//    gameList.forEach {
//        if (it.id > nextID) {
//            nextID = it.id
//        }
//    }
//    return nextID + 1
//}