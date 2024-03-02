package net.oddware.gamepadmp.android

import android.content.Context

interface AppContainer {
    val gamesRepository: GamesRepository
    val playerRepository: PlayerRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val gamesRepository: GamesRepository by lazy {
        OfflineGamesRepository(GameDB.getDB(context).gameDao())
    }

    override val playerRepository: PlayerRepository by lazy {
        OfflinePlayerRepository(GameDB.getDB(context).playerDao())
    }
}