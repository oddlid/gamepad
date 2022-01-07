package net.oddware.gamepad

import java.util.*

class ArchivedRoundModel(
    val numPlayers: Long,
    val roundID: Long,
    val date: Date,
    val name: String,
    val gameID: Long
): Comparable<ArchivedRoundModel> {
    override fun compareTo(other: ArchivedRoundModel): Int {
        //return this.date.compareTo(other.date)
        return other.date.compareTo(this.date)
    }

    override fun toString(): String {
        return "Number of players: $numPlayers, Round ID: $roundID, Date: $date, Game name: $name"
    }
}