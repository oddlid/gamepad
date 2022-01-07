package net.oddware.gamepad

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class GameViewModel(app: Application) : AndroidViewModel(app) {
    private val gameRepo: GameRepo = GameRepo.getInstance(app)

    val players = gameRepo.players
    //val playerNames = gameRepo.playerNames
    val games = gameRepo.games
    //val gameNames = gameRepo.gameNames
    val rounds = gameRepo.rounds
    //val lastInsertedRound = gameRepo.lastInsertedRound
    //val lastInsertedActiveRound = gameRepo.lastInsertedActiveRound
    val archivedRounds = gameRepo.archivedRounds

    fun addPlayer(player: Player) = viewModelScope.launch(Dispatchers.IO) {
        gameRepo.addPlayer(player)
    }

    fun deletePlayers(vararg players: Player) = viewModelScope.launch(Dispatchers.IO) {
        gameRepo.deletePlayers(*players)
    }

    //fun deleteAllPlayers() = viewModelScope.launch(Dispatchers.IO) {
    //    gameRepo.deleteAllPlayers()
    //}

    fun updatePlayer(player: Player) = viewModelScope.launch(Dispatchers.IO) {
        gameRepo.updatePlayer(player)
    }

    fun getPlayer(id: Long): LiveData<Player?> {
        val ret = MutableLiveData<Player?>()
        viewModelScope.launch(Dispatchers.IO) {
            val player = gameRepo.getPlayer(id)
            ret.postValue(player)
        }
        return ret
    }

    fun addGame(game: Game) = viewModelScope.launch(Dispatchers.IO) {
        gameRepo.addGame(game)
    }

    fun deleteGames(vararg games: Game) = viewModelScope.launch(Dispatchers.IO) {
        gameRepo.deleteGames(*games)
    }

    //fun deleteAllGames() = viewModelScope.launch(Dispatchers.IO) {
    //    gameRepo.deleteAllGames()
    //}

    fun updateGame(game: Game) = viewModelScope.launch(Dispatchers.IO) {
        gameRepo.updateGame(game)
    }

    fun getGame(id: Long): LiveData<Game?> {
        val ret = MutableLiveData<Game?>()
        viewModelScope.launch(Dispatchers.IO) {
            val game = gameRepo.getGame(id)
            ret.postValue(game)
        }
        return ret
    }

    fun addRound(round: Round): LiveData<Boolean> {
        //Timber.d("Inside GameViewModel.addRound()")
        val ret = MutableLiveData<Boolean>()
        viewModelScope.launch(Dispatchers.IO) {
            gameRepo.addRound(round)
            ret.postValue(true)
        }
        return ret
    }

    fun deleteRounds(vararg rounds: Round) = viewModelScope.launch(Dispatchers.IO) {
        gameRepo.deleteRounds(*rounds)
    }

    //fun deleteAllRounds() = viewModelScope.launch(Dispatchers.IO) {
    //    gameRepo.deleteAllRounds()
    //}

    fun updateRound(round: Round) = viewModelScope.launch(Dispatchers.IO) {
        gameRepo.updateRound(round)
    }

    fun getRound(id: Long): LiveData<Round?> {
        val ret = MutableLiveData<Round?>()
        viewModelScope.launch(Dispatchers.IO) {
            val round = gameRepo.getRound(id)
            ret.postValue(round)
        }
        return ret
    }

    fun addPoint(point: Point) = viewModelScope.launch(Dispatchers.IO) {
        gameRepo.addPoint(point)
    }

    fun addPoints(vararg points: Point) = viewModelScope.launch(Dispatchers.IO) {
        Timber.d("Inside GameViewModel.addPoints()")
        gameRepo.addPoints(*points)
    }

    //fun deletePoints(vararg points: Point) = viewModelScope.launch(Dispatchers.IO) {
    //    gameRepo.deletePoints(*points)
    //}

    //fun deleteAllPoints() = viewModelScope.launch(Dispatchers.IO) {
    //    gameRepo.deleteAllPoints()
    //}

    //fun updatePoint(point: Point) = viewModelScope.launch(Dispatchers.IO) {
    //    gameRepo.updatePoint(point)
    //}

//    fun getCurrentPoints(playerID: Long, gameID: Long, roundID: Long): LiveData<Long> {
//        val ret = MutableLiveData<Long>()
//        viewModelScope.launch(Dispatchers.IO) {
//            val points = gameRepo.getCurrentPoints(playerID, gameID, roundID)
//            ret.postValue(points)
//        }
//        return ret
//    }

//    fun getUpdatesForPlayerInRound(playerID: Long, roundID: Long): LiveData<Long> {
//        val ret = MutableLiveData<Long>()
//        viewModelScope.launch(Dispatchers.IO) {
//            val numUpdates = gameRepo.getUpdatesForPlayerInRound(playerID, roundID)
//            ret.postValue(numUpdates)
//        }
//        return ret
//    }

    fun getLastInsertedActiveRound(): LiveData<Round?> {
        val ret = MutableLiveData<Round?>()
        viewModelScope.launch(Dispatchers.IO) {
            val round = gameRepo.getLastInsertedActiveRound()
            ret.postValue(round)
        }
        return ret
    }

    fun getActivePlayerModelsForRound(
        gameID: Long,
        roundID: Long
    ): LiveData<List<ActivePlayerModel>> {
        val ret = MutableLiveData<List<ActivePlayerModel>>()

        viewModelScope.launch(Dispatchers.IO) {
            val game = gameRepo.getGame(gameID) ?: return@launch
            val round = gameRepo.getRound(roundID) ?: return@launch
            val playerIDs = gameRepo.getPlayerIDsForRound(roundID).toLongArray()
            val players = gameRepo.getPlayersWithID(*playerIDs)
            val points = gameRepo.getLastPointsForPlayersInRound(roundID, gameID, *playerIDs)
            val apmList: MutableList<ActivePlayerModel> = mutableListOf()

            for (p in players) {
                val apm = ActivePlayerModel(
                    player = p,
                    game = game,
                    round = round,
                    numUpdates = gameRepo.getUpdatesForPlayerInRound(p.playerID, round.roundID)
                )
                for (point in points) {
                    if (point.playerID == p.playerID) {
                        apm.point = point
                    }
                }
                apmList.add(apm)
            }
            ret.postValue(apmList)
        }

        return ret
    }

    //fun getArchivedRounds(): LiveData<List<ArchivedRoundModel>> {
    //    val ret = MutableLiveData<List<ArchivedRoundModel>>()
    //    viewModelScope.launch(Dispatchers.IO) {
    //        val archivedRounds = gameRepo.getArchivedRounds()
    //        ret.postValue(archivedRounds)
    //    }
    //    return ret
    //}
}