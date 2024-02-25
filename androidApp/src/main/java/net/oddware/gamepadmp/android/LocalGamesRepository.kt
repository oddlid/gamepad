package net.oddware.gamepadmp.android

import kotlinx.coroutines.flow.Flow

class LocalGamesRepository(
    private val gameDao: GameDao
) : GamesRepository {
    override fun getAllGamesStream(): Flow<List<Game>> = gameDao.getAllGames()

    override fun getGameStream(id: Int): Flow<Game?> = gameDao.getGame(id)

    override suspend fun insertGame(game: Game) = gameDao.insert(game)

    override suspend fun deleteGame(game: Game) = gameDao.delete(game)

    override suspend fun updateGame(game: Game) = gameDao.update(game)
}