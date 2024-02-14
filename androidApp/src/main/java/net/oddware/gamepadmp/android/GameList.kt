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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


enum class GameListMode {
    LIST,
    ADD,
    EDIT,
}

@Composable
fun GameListScreen(
    modifier: Modifier = Modifier,
    gameViewModel: GameViewModel = viewModel(),
    onSelect: (Int) -> Unit = {},
) {

    var mode by rememberSaveable { mutableStateOf(GameListMode.LIST) }

    when (mode) {
        GameListMode.LIST -> {
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
                        gameViewModel.games,
                        key = { _, game: Game ->
                            game.hashCode()
                        }
                    ) { _, game ->
                        EditableListItem(
                            value = game.name,
                            modifier = modifier,
                            onDelete = {
                                gameViewModel.remove(game)
                            },
                            onEdit = {
                                gameViewModel.currentID = game.id
                                mode = GameListMode.EDIT
                            },
                            onClick = { onSelect(game.id) }
                        )
                    }
                }
                FilledTonalButton(
                    onClick = {
                        mode = GameListMode.ADD
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
                title = stringResource(R.string.editItemTitleAddGame),
                modifier = modifier,
                onCancel = {
                    mode = GameListMode.LIST
                },
                onSave = { name: String ->
                    gameViewModel.add(name)
                    mode = GameListMode.LIST
                }
            )
        }

        GameListMode.EDIT -> {
            gameViewModel.find(gameViewModel.currentID)?.also {
                EditItem(
                    title = stringResource(R.string.editItemTitleEditGame, it.id),
                    value = it.name,
                    modifier = modifier,
                    onCancel = {
                        mode = GameListMode.LIST
                    },
                    onSave = { name ->
                        gameViewModel.setName(it, name)
                        mode = GameListMode.LIST
                    }
                )
            } ?: run {
                Text(
                    text = stringResource(
                        R.string.gameListGameNotFoundMessage,
                        gameViewModel.currentID
                    ),
                    modifier = modifier,
                )
            }
        }
    }

}

//@Composable
//fun GameList(
//    games: List<Game>,
//    modifier: Modifier = Modifier,
//    onDelete: (Game) -> Unit = {},
//    onEdit: (Game) -> Unit = {},
//    onClick: () -> Unit = {},
//) {
//    LazyColumn(
//        modifier = modifier
//    ) {
//        itemsIndexed(
//            games,
//            key = { _, game: Game ->
//                game.hashCode()
//            }
//        ) { _, game ->
//            EditableListItem(
//                value = game.name,
//                modifier = modifier,
//                onDelete = { onDelete(game) },
//                onEdit = { onEdit(game) },
//                onClick = onClick,
//                onSelection = { true },
//            )
//        }
//    }
//}

//@Composable
//fun Notify(text: String) {
//    val ctx = LocalContext.current
//    Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show()
//}

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