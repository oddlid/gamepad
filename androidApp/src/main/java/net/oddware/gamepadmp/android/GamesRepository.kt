package net.oddware.gamepadmp.android

import kotlinx.coroutines.flow.Flow

interface GamesRepository {
    fun getAllGamesStream(): Flow<List<Game>>

    fun getGameStream(id: Int): Flow<Game?>

    suspend fun insertGame(game: Game)

    suspend fun deleteGame(game: Game)

    suspend fun updateGame(game: Game)
}