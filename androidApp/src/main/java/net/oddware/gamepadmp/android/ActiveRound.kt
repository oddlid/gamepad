package net.oddware.gamepadmp.android

class ActiveRound(
    private val game: Game,
    private val players: List<Player>,
) {
    private val _activePlayers = players.map { ActivePlayer(it) }
//    val activePlayers: List<ActivePlayer>
//        get() = _activePlayers.sortedBy { it.sum }.reversed() // descending by points

    val name = game.name
}