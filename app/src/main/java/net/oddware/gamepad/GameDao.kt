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

    // Get specific player
    @Query("SELECT * FROM players WHERE playerID == :id")
    suspend fun getPlayer(id: Long): Player?

    // Get list of players
    @Query("SELECT * FROM players WHERE playerID IN (:playerIDs)")
    suspend fun getPlayersWithID(vararg playerIDs: Long): List<Player>

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

    // get specific game
    @Query("SELECT * FROM games WHERE gameID == :id")
    suspend fun getGame(id: Long): Game?

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

    // get specific round
    @Query("SELECT * FROM rounds WHERE roundID == :roundID")
    suspend fun getRound(roundID: Long): Round?

    // get list of rounds
    @Query("SELECT * FROM rounds")
    fun getRounds(): LiveData<List<Round>>

    // get last inserted round
    @Query("SELECT * FROM rounds ORDER BY roundID DESC LIMIT 1")
    suspend fun getLastInsertedRound(): Round?

    // get last inserted active round
    // Using LiveData here seems to make it cached, not returning updated results! BUG!
    @Query("SELECT * FROM rounds WHERE finished == 0 ORDER BY roundID DESC LIMIT 1")
    suspend fun getLastInsertedActiveRound(): Round?

    // add points
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPoint(point: Point)

    // add multiple points
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPoints(vararg points: Point)

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

    // Get a list of Point objects that are the most recent for a Round of Game with a list of Players
    @Query("SELECT DISTINCT * FROM points WHERE roundID == :roundID AND gameID == :gameID AND playerID IN (:playerIDs) GROUP BY playerID ORDER BY pointID DESC")
    suspend fun getLastPointsForPlayersInRound(
        roundID: Long,
        gameID: Long,
        vararg playerIDs: Long
    ): List<Point>

    // Get playerIDs for a given round
    @Query("SELECT DISTINCT playerID FROM points WHERE roundID == :roundID")
    suspend fun getPlayerIDsForRound(roundID: Long): List<Long>

    // Get number of updates for a given player in a given round
    @Query("SELECT COUNT(pointID) FROM points WHERE roundID == :roundID AND playerID == :playerID")
    suspend fun getUpdatesForPlayerInRound(playerID: Long, roundID: Long): Long

    // Get list of finished rounds
    // Could be done like this:
    // SELECT R.roundID, R.date, G.name FROM rounds R INNER JOIN games G ON R.gameID == G.gameID WHERE R.finished == 1 ORDER by R.date DESC
    // For join queries, we'll need to create separate data objects to hold the result

    // Get list of finished rounds, including the number of players in each round:
    // select (select count(distinct P.playerID) from points P where P.roundID == R.roundID) as numPlayers,
    // R.roundID, R.date, G.name from rounds R inner join games G on R.gameID == G.gameID  where R.finished == 1  order  by R.date desc
    @Query("SELECT (SELECT COUNT(DISTINCT P.playerID) FROM points P WHERE P.roundID == R.roundID) AS numPlayers, R.roundID, R.date, G.name, G.gameID FROM rounds R INNER JOIN games G ON R.gameID == G.gameID WHERE R.finished == 1 ORDER BY R.date DESC")
    fun getArchivedRounds(): LiveData<List<ArchivedRoundModel>>
}