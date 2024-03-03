package net.oddware.gamepadmp.android

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch


@Composable
fun GameListScreen(
    modifier: Modifier = Modifier,
    gameViewModel: GameViewModel = viewModel(),
    onSelect: (Int) -> Unit = {},
) {
    val crScope = rememberCoroutineScope()

    when (gameViewModel.currentMode.value) {
        GameViewModel.Mode.LIST -> {
            val games = gameViewModel.games.collectAsStateWithLifecycle(initialValue = emptyList())
            Column {
                Text(
                    text = "Select game",
                    modifier = modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                )
                LazyColumn(
                    modifier = modifier.weight(weight = 1F),
                ) {
                    itemsIndexed(
                        games.value,
                        key = { _, game: Game ->
                            game.hashCode()
                        }
                    ) { _, game ->
                        EditableListItem(
                            value = game.name,
                            modifier = modifier,
                            onDelete = {
                                crScope.launch {
                                    gameViewModel.remove(game)
                                }
                            },
                            onEdit = {
                                gameViewModel.currentID = game.id
                                gameViewModel.currentMode.value = GameViewModel.Mode.EDIT
                            },
                            onClick = { onSelect(game.id) },
                        )
                    }
                }
                FilledTonalButton(
                    onClick = {
                        gameViewModel.currentMode.value = GameViewModel.Mode.ADD
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

        GameViewModel.Mode.ADD -> {
            EditItem(
                title = stringResource(R.string.eiTitleAddGame),
                modifier = modifier,
                onCancel = {
                    gameViewModel.currentMode.value = GameViewModel.Mode.LIST
                },
                onSave = { name: String ->
                    crScope.launch {
                        gameViewModel.add(name)
                    }
                    gameViewModel.currentMode.value = GameViewModel.Mode.LIST
                }
            )
        }

        GameViewModel.Mode.EDIT -> {
            gameViewModel.find(gameViewModel.currentID)
                .collectAsStateWithLifecycle(initialValue = Game(0, "")).also {
                    it.value?.also {
                        EditItem(
                            title = stringResource(R.string.eiTitleEditGame, it.id),
                            value = it.name,
                            modifier = modifier,
                            onCancel = {
                                gameViewModel.currentMode.value = GameViewModel.Mode.LIST
                            },
                            onSave = { name ->
                                crScope.launch {
                                    gameViewModel.setName(it, name)
                                }
                                gameViewModel.currentMode.value = GameViewModel.Mode.LIST
                            }
                        )
                    }
                }
//            gameViewModel.find(gameViewModel.currentID)?.also {
//                EditItem(
//                    title = stringResource(R.string.eiTitleEditGame, it.id),
//                    value = it.name,
//                    modifier = modifier,
//                    onCancel = {
//                        gameViewModel.currentMode.value = GameViewModel.Mode.LIST
//                    },
//                    onSave = { name ->
//                        crScope.launch {
//                            gameViewModel.setName(it, name)
//                        }
//                        gameViewModel.currentMode.value = GameViewModel.Mode.LIST
//                    }
//                )
//            } ?: run {
//                NotFound(
//                    text = stringResource(
//                        R.string.notFoundTxtGame,
//                        gameViewModel.currentID
//                    ),
//                    onClick = {
//                        gameViewModel.currentMode.value = GameViewModel.Mode.LIST
//                    },
//                    modifier = modifier,
//                )
//            }
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