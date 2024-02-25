package net.oddware.gamepadmp.android

import androidx.compose.runtime.Immutable
import java.time.LocalDateTime

@Immutable
data class Point(
    val value: Int,
    val time: LocalDateTime,
)
