package net.oddware.gamepadmp.android

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ActivePlayerListItem(
    activePlayer: ActivePlayer,
    modifier: Modifier = Modifier,
    onAdd: (Int) -> Unit = {},
) {
    var showHistory by rememberSaveable { mutableStateOf(false) }
    var inputEnabled by rememberSaveable { mutableStateOf(false) }
    var input by rememberSaveable { mutableStateOf("") }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
        ),
    ) {
        Column(
            modifier = modifier,
        ) {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = { showHistory = !showHistory },
//                modifier = modifier,
                ) {
                    Icon(
                        imageVector = if (showHistory)
                            Icons.Filled.KeyboardArrowDown
                        else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = if (showHistory)
                            stringResource(R.string.contentDescCollapse)
                        else stringResource(R.string.contentDescExpand),
                        modifier = modifier,
                    )
                }
                TextField(
                    value = if (inputEnabled) input else "${activePlayer.currentPoints}",
                    enabled = inputEnabled,
                    onValueChange = { input = it },
                    singleLine = true,
                    modifier = modifier
                        .weight(1F)
                        .clickable(
                            onClick = {
                                inputEnabled = true
                            }
                        ),
                    label = {
                        Text(activePlayer.player.name)
                    },
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done,
                        autoCorrect = false,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            input.toIntOrNull()?.also {
                                onAdd(it)
                            }
                            input = ""
                            inputEnabled = false
                        },
                    ),
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = null,
                        )
                    },
                )
                SaveOrCancel(
//                modifier = modifier,
                    enabled = inputEnabled,
                    onSave = {
                        input.toIntOrNull()?.also {
                            onAdd(it)
                        }
                        input = ""
                        inputEnabled = false
                    },
                    onCancel = {
                        input = ""
                        inputEnabled = false
                    },
                )
            }
            if (showHistory) {
                PointHistoryList(
                    points = activePlayer.history.reversed(),
                    modifier = modifier,
                )
            }
        }
    }
}

@Composable
fun SaveOrCancel(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onSave: () -> Unit = {},
    onCancel: () -> Unit = {},
) {
    Row(
        modifier = modifier,
    ) {
        IconButton(
            modifier = modifier,
            enabled = enabled,
            onClick = onSave,
        ) {
            Icon(
                Icons.Filled.Check,
                contentDescription = stringResource(R.string.btnTxtSave),
                tint = MaterialTheme.colorScheme.surfaceTint.copy(alpha = LocalContentColor.current.alpha),
            )
        }
        IconButton(
            modifier = modifier,
            enabled = enabled,
            onClick = onCancel,
        ) {
            Icon(
                Icons.Filled.Close,
                contentDescription = stringResource(R.string.btnTxtCancel),
                tint = MaterialTheme.colorScheme.error.copy(alpha = LocalContentColor.current.alpha),
            )
        }
    }
}

object Fmt {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
}

@Composable
fun PointHistoryList(
    points: List<Point>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        points.forEach {
            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp,
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                ),
            ) {
                Row {
                    Text(
                        text = "@ ${it.time.format(Fmt.formatter)}",
                        modifier = modifier,
                    )
                    Text(
                        text = "${it.value}",
                        modifier = modifier.weight(weight = 1F),
                        textAlign = TextAlign.End,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewActivePlayerListItem() {
    MyApplicationTheme {
        Surface {
            ActivePlayerListItem(
                activePlayer = ActivePlayer(Player(0, "Sandra", true)),
                modifier = Modifier.padding(all = 4.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPointHistoryList() {
    MyApplicationTheme {
        Surface {
            PointHistoryList(
                points = List(3) { i -> Point(i, LocalDateTime.now()) },
                modifier = Modifier.padding(all = 4.dp),
            )
        }
    }
}