package net.oddware.gamepadmp.android

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Immutable
@Entity(tableName = "games")
data class Game(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
)