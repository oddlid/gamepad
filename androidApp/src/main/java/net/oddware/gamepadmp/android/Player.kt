package net.oddware.gamepadmp.android

import androidx.compose.runtime.Immutable

@Immutable
data class Player(
    val id: Int,
    val name: String,
    val selected: Boolean,
)