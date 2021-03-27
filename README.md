The idea here is to replace the pen and paper when playing games at the pub.
So we should find a way to note each player's points in a round in a given game,
preferably in a way that works for all/most games.

We should also have an archive for finished games, so one can look at the history, for those who
like to gloat.

From what I've observed this far, it seems what people note down, no matter which game, is:

- The player
- Each player's points

And from that, one just updates the points by striking out the previous sum and adding the updated
one.
So, it seems we could to with a list of players in a round, with a title of the game.
Then click on a button on a players entry, to update their points.
Click on the entire players entry to see the update history.

Players names should be saved in a table for easy picking of next rounds.
Game names should be saved in a table for easy picking of next rounds.
Points should be saved, linked to each player, for each round.

Ideally, we should have foreign keys all over the place, to keep the DB small, but I'm considering
duplicating stuff just to keep the design simpler...

Thoughts about structure:

table Players {
    ID
    Name
}

table Games {
    ID
    Name
}

// For this table, we could either get the most recent score for a player by either using the timestamp
// field and order by that, or we could have an autoincrementing ID and order by that.
// Both should not be needed.
// If we have the date saved in another table, for a round, we could use only the ID here.
table Points {
    ID
    GameID
    PlayerID
    //TimeStamp
    Points
}

table Rounds {
    ID
    TimeStamp
    GameID
    PlayerIDs
}