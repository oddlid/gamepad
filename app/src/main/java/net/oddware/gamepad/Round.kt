package net.oddware.gamepad

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "rounds")
data class Round(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "roundID")
    var roundID: Long = 0,
    @ColumnInfo(name = "date")
    var date: Date? = null,
    @ColumnInfo(name = "finished")
    var finished: Boolean = false
)
