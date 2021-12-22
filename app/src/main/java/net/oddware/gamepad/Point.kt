package net.oddware.gamepad

import androidx.room.*

@Entity(
    tableName = "points",
    foreignKeys = [
        ForeignKey(
            entity = Player::class,
            parentColumns = ["playerID"], // points to playerID field in Player class
            childColumns = ["playerID"],  // points to playerID field in this class
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Game::class,
            parentColumns = ["gameID"], // points to gameID field in Game class
            childColumns = ["gameID"],  // points to gameID field in this class
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Round::class,
            parentColumns = ["roundID"], // points to roundID field in Round class
            childColumns = ["roundID"],  // points to roundID field in this class
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            value = ["playerID"],
            unique = false
        ),
        Index(
            value = ["gameID"],
            unique = false
        ),
        Index(
            value = ["roundID"],
            unique = false
        )
    ]
)
data class Point(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pointID")
    var pointID: Long = 0,
    @ColumnInfo(name = "playerID")
    var playerID: Long = 0,
    @ColumnInfo(name = "gameID")
    var gameID: Long = 0,
    @ColumnInfo(name = "roundID")
    var roundID: Long = 0,
    @ColumnInfo(name = "value")
    var value: Long = 0
)
