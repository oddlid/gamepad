package net.oddware.gamepadmp.android

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    private val _games = getGames().toMutableStateList()
    val games: List<Game>
        get() = _games

    var currentID: Int = -1

    fun find(id: Int): Game? = _games.find { it.id == id }

    fun remove(game: Game) {
        _games.remove(game)
    }

    fun add(name: String) {
        _games.add(Game(getNextGameID(_games), name))
    }

    fun setName(game: Game, name: String) {
        _games.find { it.id == game.id }?.let {
//            _games.remove(it)
//            it.name = name
//            _games.add(it)
//            it.name = name
            _games.remove(it)
            _games.add(it.copy(name = name))
        }
    }
}

private fun getGames() = List(3) { i -> Game(i, "Game # $i") }

private fun getNextGameID(gameList: List<Game>): Int {
    var nextID: Int = -1
    for (game in gameList) {
        if (game.id > nextID) {
            nextID = game.id
        }
    }
    return nextID + 1
}