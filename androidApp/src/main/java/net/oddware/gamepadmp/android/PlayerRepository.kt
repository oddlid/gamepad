package net.oddware.gamepadmp.android

import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    fun getAllPlayersStream(): Flow<List<Player>>

    fun getPlayerStream(id: Int): Flow<Player?>

    suspend fun insertPlayer(player: Player)

    suspend fun deletePlayer(player: Player)

    suspend fun updatePlayer(player: Player)

    suspend fun selectAll(selected: Boolean)

    suspend fun toggleSelection(id: Int)

    fun hasSelection(): Flow<Boolean>

    fun allSelected(): Flow<Boolean>
}