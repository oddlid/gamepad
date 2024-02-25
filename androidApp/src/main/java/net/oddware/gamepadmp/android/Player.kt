package net.oddware.gamepadmp.android

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Immutable
@Entity(tableName = "players")
data class Player(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    @Ignore
    val selected: Boolean = false,
)