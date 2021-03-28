package net.oddware.gamepad

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class Player(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "playerID")
    var playerID: Long = 0,
    @ColumnInfo(name = "name")
    var name: String = ""
)
