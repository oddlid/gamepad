package net.oddware.gamepadmp.android

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(player: Player)

    @Update
    suspend fun update(player: Player)

    @Delete
    suspend fun delete(player: Player)

    @Query("UPDATE players SET selected = :selected")
    suspend fun selectAll(selected: Boolean)

    @Query("UPDATE players SET selected = NOT selected WHERE id = :id")
    suspend fun toggleSelection(id: Int)

    @Query("SELECT * FROM players WHERE id = :id")
    fun getPlayer(id: Int): Flow<Player>

    @Query("SELECT * FROM players ORDER BY name ASC")
    fun getAllPlayers(): Flow<List<Player>>

    @Query("SELECT * FROM players WHERE selected = :selected")
    suspend fun filterBySelection(selected: Boolean): List<Player>

    @Query("SELECT COUNT(selected) FROM players WHERE selected = 1")
    fun hasSelection(): Flow<Boolean>

//    @Query("SELECT COUNT(DISTINCT selected) = 1 AS result FROM players WHERE selected = 1")
//    fun allSelected(): Flow<Boolean>
}