package net.oddware.gamepadmp.android

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(game: Game)

    @Update
    suspend fun update(game: Game)

    @Delete
    suspend fun delete(game: Game)

    @Query("SELECT * FROM games WHERE id = :id")
    fun getGame(id: Int): Flow<Game>

    @Query("SELECT * FROM games ORDER BY name ASC")
    fun getAllGames(): Flow<List<Game>>
}