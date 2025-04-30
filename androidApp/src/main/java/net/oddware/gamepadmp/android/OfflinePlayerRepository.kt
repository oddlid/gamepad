package net.oddware.gamepadmp.android

import kotlinx.coroutines.flow.Flow

class OfflinePlayerRepository(private val playerDao: PlayerDao) : PlayerRepository {
    override fun getAllPlayersStream(): Flow<List<Player>> = playerDao.getAllPlayers()

    override fun getPlayerStream(id: Int): Flow<Player?> = playerDao.getPlayer(id)

    override suspend fun insertPlayer(player: Player) = playerDao.insert(player)

    override suspend fun deletePlayer(player: Player) = playerDao.delete(player)

    override suspend fun updatePlayer(player: Player) = playerDao.update(player)

    override suspend fun selectAll(selected: Boolean) = playerDao.selectAll(selected)

    override suspend fun toggleSelection(id: Int) = playerDao.toggleSelection(id)

    override fun hasSelection() = playerDao.hasSelection()

    override fun allSelected(): Flow<Boolean> = playerDao.allSelected()

    override suspend fun filterBySelection(selected: Boolean) =
        playerDao.filterBySelection(selected)

}