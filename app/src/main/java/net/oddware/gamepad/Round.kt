package net.oddware.gamepad

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "rounds",
    foreignKeys = [
        ForeignKey(
            entity = Game::class,
            parentColumns = ["gameID"],
            childColumns = ["gameID"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Round(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "roundID")
    var roundID: Long = 0,
    @ColumnInfo(name = "gameID")
    var gameID: Long = 0,
    @ColumnInfo(name = "date")
    var date: Date? = null,
    @ColumnInfo(name = "finished")
    var finished: Boolean = false
)
