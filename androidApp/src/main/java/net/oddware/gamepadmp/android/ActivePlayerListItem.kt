package net.oddware.gamepadmp.android

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ActivePlayerListItem(
    activePlayer: ActivePlayer,
    modifier: Modifier = Modifier,
    onAdd: (Int) -> Unit = {},
) {
    var showHistory by rememberSaveable { mutableStateOf(false) }
    var inputInProgress by rememberSaveable { mutableStateOf(false) }
    var isError by rememberSaveable { mutableStateOf(false) }
    var input by rememberSaveable { mutableStateOf("") }
    var value by rememberSaveable { mutableIntStateOf(0) }
    val focusMgr = LocalFocusManager.current

    fun clear() {
        value = 0
        input = ""
        focusMgr.clearFocus()
        isError = false
    }

    fun save() {
        // we don't want a history entry of 0
        if (value != 0) {
            onAdd(value)
        }
        clear()
    }

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
                OutlinedTextField(
                    value = if (inputInProgress) input else "${activePlayer.currentPoints}",
                    onValueChange = {
                        input = it
                        // don't show error when beginning to enter a negative value
                        if (input == "-") {
                            return@OutlinedTextField
                        }
                        val maybeValue = input.toIntOrNull()
                        if (maybeValue == null) {
                            isError = true
                        } else {
                            value = maybeValue
                            isError = false
                        }
                    },
                    singleLine = true,
                    isError = isError,
                    supportingText = {
                        if (isError) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.errInvalidValue),
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    },
                    modifier = modifier
                        .weight(1F)
                        .onFocusChanged {
                            inputInProgress = it.isFocused
                        },
                    label = {
                        Text(activePlayer.player.name)
                    },
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Unspecified,
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done,
                        showKeyboardOnFocus = true,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            save()
                        },
                    ),
                    leadingIcon = {
                        if (isError) {
                            Icon(Icons.Filled.Warning, contentDescription = "error")
                        } else {
                            if (activePlayer.player.iconUri != null) {
                                AsyncImage(
                                    model = activePlayer.player.iconUri,
                                    contentDescription = activePlayer.player.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = modifier
                                        .width(24.dp)
                                        .height(24.dp)
                                        .clip(CircleShape)
                                )
                            } else {
                                Icon(
                                    Icons.Filled.Person,
                                    contentDescription = null,
                                )
                            }
                        }
                    },
                )
                SaveOrCancel(
//                modifier = modifier,
                    enabled = inputInProgress,
                    onSave = { save() },
                    onCancel = { clear() },
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

@Preview(showBackground = true, apiLevel = 35)
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

@Preview(showBackground = true, apiLevel = 35)
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