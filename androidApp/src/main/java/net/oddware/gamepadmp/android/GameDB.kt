package net.oddware.gamepadmp.android

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE players ADD COLUMN iconUri TEXT")
    }
}

@Database(
    version = 2,
    exportSchema = false,
    entities = [
        Game::class,
        Player::class,
    ],
)
@TypeConverters(Converters::class)
abstract class GameDB : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun playerDao(): PlayerDao

    companion object {
        @Volatile
        private var Instance: GameDB? = null

        fun getDB(context: Context): GameDB {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, GameDB::class.java, "game_db")
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration(false)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}