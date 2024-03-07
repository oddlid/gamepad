package net.oddware.gamepadmp.android

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun GameListScreen(
    modifier: Modifier = Modifier,
    gameViewModel: GameViewModel = viewModel(),
    onSelect: (Game) -> Unit = {},
) {
    val uiState: GameScreenUIState by gameViewModel.uiState.collectAsStateWithLifecycle()
    val gameListState = rememberLazyListState()

    when (uiState.mode) {
        GameListMode.LIST -> {
            Column {
                Text(
                    text = stringResource(R.string.lblTxtSelectGame),
                    modifier = modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                )
                Row(
                    modifier = modifier.weight(1F),
                ) {
                    GameList(
                        uiState = uiState.games,
                        modifier = modifier,
                        onSelect = onSelect,
                        onEdit = { gameViewModel.onEdit(it) },
                        onDelete = { gameViewModel.onDelete(it) },
                        listState = gameListState,
                    )
                }
                FilledTonalButton(
                    onClick = {
                        gameViewModel.onAdd()
                    },
                    modifier = modifier.fillMaxWidth(),
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = stringResource(R.string.btnTxtAdd),
                    )
                    Text(text = stringResource(R.string.btnTxtAdd))
                }
            }
        }

        GameListMode.ADD -> {
            EditItem(
                title = stringResource(R.string.eiTitleAddGame),
                modifier = modifier,
                onCancel = {
                    gameViewModel.onCancel()
                },
                onSave = { name: String ->
                    gameViewModel.onSaveNew(name)
                }
            )
        }

        GameListMode.EDIT -> {
            EditItem(
                title = stringResource(R.string.eiTitleEditGame, uiState.currentGame.id),
                value = uiState.currentGame.name,
                modifier = modifier,
                onCancel = { gameViewModel.onCancel() },
                onSave = { gameViewModel.onUpdate(uiState.currentGame, it) },
            )
        }
    }

}

@Composable
fun GameList(
    uiState: GameListUiState,
    modifier: Modifier = Modifier,
    onSelect: (Game) -> Unit = {},
    onEdit: (Game) -> Unit = {},
    onDelete: (Game) -> Unit = {},
    listState: LazyListState = rememberLazyListState(),
) {
    LazyColumn(
        modifier = modifier,
        state = listState,
    ) {
        when (uiState) {
            GameListUiState.Error -> {
                item {
                    Text(
                        modifier = modifier,
                        text = "Error loading games",
                    )
                }
            }

            GameListUiState.Loading -> {
                item {
                    LoadingIndicator(modifier = modifier)
                }
            }

            is GameListUiState.Success -> {
                items(uiState.games) { game ->
                    EditableListItem(
                        value = game.name,
                        modifier = modifier,
                        onClick = { onSelect(game) },
                        onEdit = { onEdit(game) },
                        onDelete = { onDelete(game) },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGameListScreen() {
    MyApplicationTheme {
        Surface {
            GameListScreen(
                modifier = Modifier.padding(all = 4.dp)
            )
        }
    }
}