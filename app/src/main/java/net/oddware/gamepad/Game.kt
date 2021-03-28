package net.oddware.gamepad

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class Game(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "gameID")
    var gameID: Long = 0,
    @ColumnInfo(name = "name")
    var name: String = ""
)
