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

    val games = gamesRepository.getAllGamesStream()

    var currentID: Int = -1
    val currentMode: MutableState<Mode> = mutableStateOf(Mode.LIST)

    fun find(id: Int) = gamesRepository.getGameStream(id)

    suspend fun remove(game: Game) {
        gamesRepository.deleteGame(game)
    }

    suspend fun add(name: String) {
        gamesRepository.insertGame(Game(name = name))
    }

    suspend fun setName(game: Game, name: String) {
        gamesRepository.updateGame(game.copy(name = name))
    }
}