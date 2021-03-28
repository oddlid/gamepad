package net.oddware.gamepad

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        Player::class,
        Game::class,
        Round::class,
        Point::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GameDB : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: GameDB? = null

        fun get(ctx: Context): GameDB {
            val tmpInst = INSTANCE
            if (null != tmpInst) {
                return tmpInst
            }
            synchronized(this) {
                val inst = Room.databaseBuilder(
                    ctx.applicationContext,
                    GameDB::class.java,
                    "gamepaddb"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = inst
                return inst
            }
        }
    }

    abstract fun gameDao(): GameDao
}