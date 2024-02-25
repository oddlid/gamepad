package net.oddware.gamepadmp.android

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        Game::class,
        Player::class,
    ]
)
abstract class GameDB : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun playerDao(): PlayerDao

    companion object {
        @Volatile
        private var Instance: GameDB? = null

        fun getDB(context: Context): GameDB {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, GameDB::class.java, "game_db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}