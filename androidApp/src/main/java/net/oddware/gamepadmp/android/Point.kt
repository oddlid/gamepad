package net.oddware.gamepadmp.android

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.toMutableStateList
import java.time.LocalDateTime

@Immutable
data class Point(val value: Int, val time: LocalDateTime)

class Points {
    private val _history = mutableListOf<Point>().toMutableStateList()
    private val _currentPoints = mutableIntStateOf(0)

    val history: List<Point>
        get() = _history.reversed() // newest first

    val sum: State<Int>
        get() = _currentPoints

    fun add(value: Int) {
        _history.add(Point(value, LocalDateTime.now()))
        _currentPoints.intValue = sumPoints()
        Log.i("Points", "Added Point with value: $value")
//        Log.i("Points", "History size: ${_history.size}")
    }

    private fun sumPoints(): Int {
        var sum = 0
        _history.forEach {
            sum += it.value
        }
        return sum
    }
}