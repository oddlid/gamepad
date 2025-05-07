package net.oddware.gamepadmp.android

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage


@Composable
fun PlayerListScreen(
    modifier: Modifier = Modifier,
    playerViewModel: PlayerViewModel = viewModel(),
    onBack: () -> Unit = {},
    onPlay: () -> Unit = {},
) {
    val uiState: PlayerScreenUIState by playerViewModel.uiState.collectAsStateWithLifecycle()

    when (uiState.mode) {
        PlayerListMode.LIST -> {
            Column {
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp,
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                ) {
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
                }
                Row(
                    modifier = modifier.weight(1F),
                ) {
                    PlayerList(
                        uiState = uiState.players,
                        modifier = modifier,
                        onDelete = { player ->
                            playerViewModel.onDelete(player)
                        },
                        onEdit = { player ->
                            playerViewModel.onEdit(player)
                        },
                        onSelect = { player ->
                            playerViewModel.onSelect(player)
                        },
                    )
                }
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp,
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier,
                    ) {
                        Checkbox(
                            checked = uiState.allSelected,
                            onCheckedChange = { playerViewModel.onSelectAll(!uiState.allSelected) },
                        )
                        Text(stringResource(R.string.chkTxtSelectAll))
                        Spacer(modifier = modifier.weight(weight = 1F))
                        FilledTonalButton(
                            onClick = {
                                playerViewModel.onAdd()
                            },
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
                        enabled = uiState.hasSelection,
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
        }

        PlayerListMode.ADD -> {
            EditItem(
                title = stringResource(R.string.eiLblAddPlayer),
                modifier = modifier,
                onCancel = {
                    playerViewModel.onCancel()
                },
                onSave = { name: String, uri: Uri? ->
                    playerViewModel.onSaveNew(name, uri)
                },
                imageEnabled = true,
            )
        }

        PlayerListMode.EDIT -> {
            EditItem(
                title = stringResource(R.string.eiTitleEditPlayer, uiState.currentPlayer.id),
                value = uiState.currentPlayer.name,
                modifier = modifier,
                onCancel = { playerViewModel.onCancel() },
                onSave = { name: String, uri: Uri? ->
                    playerViewModel.onUpdate(uiState.currentPlayer, name, uri)
                },
                imageEnabled = true,
                iconUri = uiState.currentPlayer.iconUri,
            )
        }
    }
}


@Composable
fun PlayerList(
    uiState: PlayerListUIState,
    modifier: Modifier = Modifier,
    onDelete: (Player) -> Unit = {},
    onEdit: (Player) -> Unit = {},
    onSelect: (Player) -> Unit = {},
    listState: LazyListState = rememberLazyListState(),
) {
    LazyColumn(
        modifier = modifier,
        state = listState,
    ) {
        when (uiState) {
            PlayerListUIState.Error -> {
                item {
                    LoadingError(
                        modifier = modifier,
                        text = stringResource(R.string.errTxtLoadPlayers),
                    )
                }
            }

            PlayerListUIState.Loading -> {
                item {
                    LoadingIndicator(modifier = modifier)
                }
            }

            is PlayerListUIState.Success -> {
                itemsIndexed(
                    uiState.players,
                    key = { _, player: Player ->
                        player.hashCode()
                    }
                ) { _, player ->
                    EditableListItem(
                        value = player.name,
                        modifier = modifier,
                        onDelete = { onDelete(player) },
                        onEdit = { onEdit(player) },
                        onClick = { onSelect(player) },
                        onSelection = { player.selected },
                        itemIcon = {
                            if (player.iconUri != null) {
                                AsyncImage(
                                    model = player.iconUri,
                                    contentDescription = player.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = modifier
                                        .width(24.dp)
                                        .height(24.dp)
                                        .clip(CircleShape)
                                )
                            } else {
                                Icon(
                                    Icons.Filled.Person,
                                    contentDescription = player.name,
                                    modifier = modifier,
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun PreviewPlayerListScreen() {
//    MyApplicationTheme {
//        Surface {
//            PlayerList(
//                players = mutableListOf<Player>().apply {
//                    repeat(3) {
//                        add(Player(it, "Player $it", false))
//                    }
//                },
//                modifier = Modifier.padding(all = 4.dp),
//            )
//        }
//    }
//}