package net.oddware.gamepadmp.android

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch


@Composable
fun PlayerListScreen(
    modifier: Modifier = Modifier,
    playerViewModel: PlayerViewModel = viewModel(),
    onBack: () -> Unit = {},
    onPlay: () -> Unit = {},
) {
    val crScope = rememberCoroutineScope()

    when (playerViewModel.currentMode.value) {
        PlayerViewModel.Mode.LIST -> {
            val players =
                playerViewModel.players.collectAsStateWithLifecycle(initialValue = emptyList())
            val hasSelection =
                playerViewModel.hasSelection().collectAsStateWithLifecycle(initialValue = false)

            PlayerList(
                players = players.value,
                modifier = modifier,
                onBack = onBack,
                onPlay = onPlay,
                onDelete = { player ->
                    crScope.launch {
                        playerViewModel.remove(player)
                    }
                },
                onEdit = { player ->
                    playerViewModel.currentID = player.id
                    playerViewModel.currentMode.value = PlayerViewModel.Mode.EDIT
                },
                onAdd = {
                    playerViewModel.currentMode.value = PlayerViewModel.Mode.ADD
                },
                onClickEntry = { player ->
                    crScope.launch {
                        playerViewModel.toggleSelection(player.id)
                    }
                },
                hasSelection = hasSelection.value,
                selectAllChecked = false,
                onToggleSelected = { selected ->
                    crScope.launch {
                        playerViewModel.selectAll(selected)
                    }
                }
            )
        }

        PlayerViewModel.Mode.ADD -> {
            EditItem(
                title = stringResource(R.string.eiLblAddPlayer),
                modifier = modifier,
                onCancel = {
                    playerViewModel.currentMode.value = PlayerViewModel.Mode.LIST
                },
                onSave = { name: String ->
                    crScope.launch {
                        playerViewModel.add(name)
                    }
                    playerViewModel.currentMode.value = PlayerViewModel.Mode.LIST
                }
            )
        }

        PlayerViewModel.Mode.EDIT -> {
            playerViewModel.find(playerViewModel.currentID)
                .collectAsStateWithLifecycle(initialValue = Player()).also { state ->
                    state.value?.also {
                        EditItem(
                            title = stringResource(R.string.eiTitleEditPlayer, it.id),
                            value = it.name,
                            modifier = modifier,
                            onCancel = {
                                playerViewModel.currentMode.value = PlayerViewModel.Mode.LIST
                            },
                            onSave = { name ->
                                crScope.launch {
                                    playerViewModel.setName(it, name)
                                }
                                playerViewModel.currentMode.value = PlayerViewModel.Mode.LIST
                            }
                        )
                    }
                }
//            playerViewModel.find(playerViewModel.currentID)?.also {
//                EditItem(
//                    title = stringResource(R.string.eiLblEditPlayer),
//                    value = it.name,
//                    modifier = modifier,
//                    onCancel = {
//                        playerViewModel.currentMode.value = PlayerViewModel.Mode.LIST
//                    },
//                    onSave = { name: String ->
//                        playerViewModel.setName(it, name)
//                        playerViewModel.currentMode.value = PlayerViewModel.Mode.LIST
//                    }
//                )
//            } ?: run {
//                NotFound(
//                    text = stringResource(R.string.notFoundTxtPlayer, playerViewModel.currentID),
//                    onClick = {
//                        playerViewModel.currentMode.value = PlayerViewModel.Mode.LIST
//                    },
//                    modifier = modifier,
//                )
//            }
        }
    }
}

@Composable
fun NotFound(
    modifier: Modifier = Modifier,
    text: String = "Not found",
    btnText: String = stringResource(R.string.btnTxtBack),
    onClick: () -> Unit = {},
) {
    Column {
        Text(
            text = text,
            modifier = modifier
        )
        Button(onClick = onClick) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = btnText,
            )
            Text(text = btnText)
        }
    }
}

@Composable
fun PlayerList(
    players: List<Player>,
    modifier: Modifier = Modifier,
    onDelete: (Player) -> Unit = {},
    onEdit: (Player) -> Unit = {},
    onClickEntry: (Player) -> Unit = {},
    onAdd: () -> Unit = {},
    onToggleSelected: (Boolean) -> Unit = {},
    onPlay: () -> Unit = {},
    onBack: () -> Unit = {},
    hasSelection: Boolean = false,
    selectAllChecked: Boolean = false,
) {
    Column {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = stringResource(R.string.lblTxtSelectPlayers),
                modifier = modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
            )
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.btnTxtBack),
                )
            }
        }
        LazyColumn(
            modifier = modifier.weight(weight = 1F)
        ) {
            itemsIndexed(
                players,
                key = { _, player: Player ->
                    player.hashCode()
                }
            ) { _, player ->
                EditableListItem(
                    value = player.name,
                    modifier = modifier,
                    onDelete = {
                        onDelete(player)
                    },
                    onEdit = {
                        onEdit(player)
                    },
                    onClick = {
                        onClickEntry(player)
                    },
                    onSelection = { player.selected },
                    itemIcon = {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = "",
                            modifier = modifier,
                        )
                    }
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier,
        ) {
            Checkbox(
                checked = selectAllChecked,
                onCheckedChange = onToggleSelected,
            )
            Text(stringResource(R.string.chkTxtSelectAll))
            Spacer(modifier = modifier.weight(weight = 1F))
            FilledTonalButton(
                onClick = onAdd,
                modifier = modifier,
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = stringResource(R.string.btnTxtAdd),
                )
                Text(text = stringResource(R.string.btnTxtAdd))
            }
        }
        FilledTonalButton(
            modifier = modifier.fillMaxWidth(),
            enabled = hasSelection,
            onClick = onPlay,
        ) {
            Icon(
                Icons.Filled.PlayArrow,
                contentDescription = stringResource(R.string.btnTxtPlay),
            )
            Text(text = stringResource(R.string.btnTxtPlay))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewNotFound() {
    MyApplicationTheme {
        Surface {
            NotFound(modifier = Modifier.padding(all = 4.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPlayerListScreen() {
    MyApplicationTheme {
        Surface {
            PlayerList(
                players = mutableListOf<Player>().apply {
                    repeat(3) {
                        add(Player(it, "Player $it", false))
                    }
                },
                modifier = Modifier.padding(all = 4.dp),
            )
        }
    }
}