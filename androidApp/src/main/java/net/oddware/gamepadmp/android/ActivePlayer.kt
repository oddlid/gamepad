package net.oddware.gamepadmp.android

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.toMutableStateList
import java.time.LocalDateTime

// ActivePlayer is a wrapper for Player + Points
class ActivePlayer(
    val player: Player,
) {
    val history = mutableListOf<Point>().toMutableStateList()
    val currentPoints by derivedStateOf { history.sumOf { it.value } }

    fun add(value: Int) {
        history.add(Point(value, LocalDateTime.now()))
    }
}