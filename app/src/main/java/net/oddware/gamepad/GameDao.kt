package net.oddware.gamepad

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GameDao {
    // add player
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPlayer(player: Player)

    // delete player(s)
    @Delete
    suspend fun deletePlayers(vararg players: Player)

    // delete all players
    @Query("DELETE FROM players")
    suspend fun deleteAllPlayers()

    // update player
    @Update
    suspend fun updatePlayer(player: Player)

    // get list of players
    @Query("SELECT * FROM players")
    fun getPlayers(): LiveData<List<Player>>

    // get list of player names
    @Query("SELECT name FROM players ORDER BY name ASC")
    fun getPlayerNames(): LiveData<List<String>>

    // add game
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addGame(game: Game)

    // delete games
    @Delete
    suspend fun deleteGames(vararg games: Game)

    // delete all games
    @Query("DELETE FROM games")
    suspend fun deleteAllGames()

    // update game
    @Update
    suspend fun updateGame(game: Game)

    // get list of games
    @Query("SELECT * FROM games")
    fun getGames(): LiveData<List<Game>>

    // get list of game names
    @Query("SELECT name FROM games ORDER BY name ASC")
    fun getGameNames(): LiveData<List<String>>

    // add round
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRound(round: Round)

    // delete rounds
    @Delete
    suspend fun deleteRounds(vararg rounds: Round)

    // delete all rounds
    @Query("DELETE FROM rounds")
    suspend fun deleteAllRounds()

    // update round
    @Update
    suspend fun updateRound(round: Round)

    // get list of rounds
    @Query("SELECT * FROM rounds")
    fun getRounds(): LiveData<List<Round>>

    // add points
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPoint(point: Point)

    // delete points
    @Delete
    suspend fun deletePoints(vararg points: Point)

    // delete all points
    @Query("DELETE FROM points")
    suspend fun deleteAllPoints()

    // update points
    @Update
    suspend fun updatePoint(point: Point)

    // get last point value for a given player in a given game in a given round
    // I first thought about using the SQL function MAX here, but since a player may go up and down
    // in points, that wouldn't work
    @Query("SELECT value FROM points WHERE playerID == :playerID AND gameID == :gameID AND roundID == :roundID ORDER BY pointID DESC LIMIT 1")
    suspend fun getCurrentPoints(playerID: Long, gameID: Long, roundID: Long): Long
}