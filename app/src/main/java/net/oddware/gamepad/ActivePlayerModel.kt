package net.oddware.gamepad

import timber.log.Timber

/*
The idea here is to wrap Player, Round, Game and Point in a way that makes it easy to use as a
list item in ActiveRoundAdapter.
 */
class ActivePlayerModel(
    val player: Player,
    val game: Game,
    val round: Round
) : Comparable<ActivePlayerModel> {
    //companion object {
    //    fun startRound(game: Game, vararg players: Player): Pair<Round, List<Point>> {
    //        val round = Round(date = Date())
    //        val points: MutableList<Point> = mutableListOf()
    //        for (pl in players) {
    //            points.add(Point(
    //                playerID = pl.playerID,
    //                gameID = game.gameID,
    //                value = 0L
    //            ))
    //        }
    //    }
    //}
    var point: Point? = null

    fun getTotalPoints(): Long {
        return point?.value ?: 0
    }

    fun updatePoints(value: Long): Point {
        val p = Point(
            playerID = player.playerID,
            gameID = game.gameID,
            roundID = round.roundID,
            value = getTotalPoints() + value
        )
        point = p
        Timber.d("${player.name} now has ${p.value} points in round of ${game.name}")
        return p
    }

    override fun compareTo(other: ActivePlayerModel): Int {
        //return when {
        //    this.getTotalPoints() > other.getTotalPoints() -> 1
        //    this.getTotalPoints() < other.getTotalPoints() -> -1
        //    else -> 0
        //}
        // This logging made it evident that no comparison happens when we update a players points and call
        // notifyItemChanged. It only happens when we load the list the first time. So we need something more...
        // Turns out the solution was to not call notifyItemChanged ourselves, but rather call
        // SortedList.updateItemAt(...), which does the right thing.
        Timber.d("This points: ${this.getTotalPoints()}, other points: ${other.getTotalPoints()}")

        // This will sort ascending
        //return this.getTotalPoints().compareTo(other.getTotalPoints())

        // I'd rather prefer descending
        return other.getTotalPoints().compareTo(this.getTotalPoints())
    }

}