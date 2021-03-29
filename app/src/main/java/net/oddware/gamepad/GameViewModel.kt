package net.oddware.gamepad

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameViewModel(app: Application) : AndroidViewModel(app) {
    private val gameRepo: GameRepo = GameRepo.getInstance(app)

    val players = gameRepo.players
    val playerNames = gameRepo.playerNames
    val games = gameRepo.games
    val gameNames = gameRepo.gameNames
    val rounds = gameRepo.rounds

    fun addPlayer(player: Player) = viewModelScope.launch(Dispatchers.IO) {
        gameRepo.addPlayer(player)
    }

    fun deletePlayers(vararg players: Player) = viewModelScope.launch(Dispatchers.IO) {
        gameRepo.deletePlayers(*players)
    }

    fun deleteAllPlayers() = viewModelScope.launch(Dispatchers.IO) {
        gameRepo.deleteAllPlayers()
    }

    fun updatePlayer(player: Player) = viewModelScope.launch(Dispatchers.IO) {
        gameRepo.updatePlayer(player)
    }

    fun addGame(game: Game) = viewModelScope.launch(Dispatchers.IO) {
        gameRepo.addGame(game)
    }

    fun deleteGames(vararg games: Game) = viewModelScope.launch(Dispatchers.IO) {
        gameRepo.deleteGames(*games)
    }

    fun deleteAllGames() = viewModelScope.launch(Dispatchers.IO) {
        gameRepo.deleteAllGames()
    }

    fun updateGame(game: Game) = viewModelScope.launch(Dispatchers.IO) {
        gameRepo.updateGame(game)
    }

    fun addRound(round: Round) = viewModelScope.launch(Dispatchers.IO) {
        gameRepo.addRound(round)
    }

    fun deleteRounds(vararg rounds: Round) = viewModelScope.launch(Dispatchers.IO) {
        gameRepo.deleteRounds(*rounds)
    }

    fun deleteAllRounds() = viewModelScope.launch(Dispatchers.IO) {
        gameRepo.deleteAllRounds()
    }

    fun updateRound(round: Round) = viewModelScope.launch(Dispatchers.IO) {
        gameRepo.updateRound(round)
    }

    fun addPoint(point: Point) = viewModelScope.launch(Dispatchers.IO) {
        gameRepo.addPoint(point)
    }

    fun deletePoints(vararg points: Point) = viewModelScope.launch(Dispatchers.IO) {
        gameRepo.deletePoints(*points)
    }

    fun deleteAllPoints() = viewModelScope.launch(Dispatchers.IO) {
        gameRepo.deleteAllPoints()
    }

    fun updatePoint(point: Point) = viewModelScope.launch(Dispatchers.IO) {
        gameRepo.updatePoint(point)
    }

    fun getCurrentPoints(playerID: Long, gameID: Long, roundID: Long): LiveData<Long> {
        val ret = MutableLiveData<Long>()
        viewModelScope.launch(Dispatchers.IO) {
            val points = gameRepo.getCurrentPoints(playerID, gameID, roundID)
            ret.postValue(points)
        }
        return ret
    }

    // ...
}