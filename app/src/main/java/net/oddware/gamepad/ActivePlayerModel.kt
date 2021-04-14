package net.oddware.gamepad

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
        return p
    }

    override fun compareTo(other: ActivePlayerModel): Int {
        return when {
            this.getTotalPoints() > other.getTotalPoints() -> 1
            this.getTotalPoints() < other.getTotalPoints() -> -1
            else -> 0
        }
    }

}