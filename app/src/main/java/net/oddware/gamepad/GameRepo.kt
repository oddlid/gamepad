package net.oddware.gamepad

import android.content.Context
import timber.log.Timber

class GameRepo(private val gameDao: GameDao) {
    companion object {
        @Volatile
        private var INSTANCE: GameRepo? = null

        fun getInstance(ctx: Context): GameRepo {
            val tmpInst = INSTANCE
            if (null != tmpInst) {
                return tmpInst
            }
            synchronized(this) {
                val inst = GameRepo(GameDB.get(ctx).gameDao())
                INSTANCE = inst
                return inst
            }
        }
    }

    val players = gameDao.getPlayers()
    val playerNames = gameDao.getPlayerNames()
    val games = gameDao.getGames()
    val gameNames = gameDao.getGameNames()
    val rounds = gameDao.getRounds()
    val lastInsertedRound = gameDao.getLastInsertedRound()
    val lastInsertedActiveRound = gameDao.getLastInsertedActiveRound()

    suspend fun addPlayer(player: Player) = gameDao.addPlayer(player)

    suspend fun deletePlayers(vararg players: Player) = gameDao.deletePlayers(*players)

    suspend fun deleteAllPlayers() = gameDao.deleteAllPlayers()

    suspend fun updatePlayer(player: Player) = gameDao.updatePlayer(player)

    suspend fun getPlayer(id: Long): Player? = gameDao.getPlayer(id)

    suspend fun getPlayersWithID(vararg playerIDs: Long) = gameDao.getPlayersWithID(*playerIDs)

    suspend fun addGame(game: Game) = gameDao.addGame(game)

    suspend fun deleteGames(vararg games: Game) = gameDao.deleteGames(*games)

    suspend fun deleteAllGames() = gameDao.deleteAllGames()

    suspend fun updateGame(game: Game) {
        Timber.d("Updating game: \"${game.name}\"")
        gameDao.updateGame(game)
    }

    suspend fun getGame(id: Long): Game? = gameDao.getGame(id)

    suspend fun addRound(round: Round) = gameDao.addRound(round)

    suspend fun deleteRounds(vararg rounds: Round) = gameDao.deleteRounds(*rounds)

    suspend fun deleteAllRounds() = gameDao.deleteAllRounds()

    suspend fun updateRound(round: Round) = gameDao.updateRound(round)

    suspend fun getRound(roundID: Long) = gameDao.getRound(roundID)

    suspend fun addPoint(point: Point) = gameDao.addPoint(point)

    suspend fun addPoints(vararg points: Point) = gameDao.addPoints(*points)

    suspend fun deletePoints(vararg points: Point) = gameDao.deletePoints(*points)

    suspend fun deleteAllPoints() = gameDao.deleteAllPoints()

    suspend fun updatePoint(point: Point) = gameDao.updatePoint(point)

    suspend fun getCurrentPoints(playerID: Long, gameID: Long, roundID: Long): Long {
        return gameDao.getCurrentPoints(playerID, gameID, roundID)
    }

    suspend fun getPlayerIDsForRound(roundID: Long): List<Long> =
        gameDao.getPlayerIDsForRound(roundID)
}