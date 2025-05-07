package net.oddware.gamepadmp.android

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameRoundScreen(
    modifier: Modifier = Modifier,
    gameRoundViewModel: GameRoundViewModel = viewModel(),
    onBack: () -> Unit = {},
) {
    val uiState = gameRoundViewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier,
    ) {
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
                    text = uiState.value.game.name,
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
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .weight(weight = 1F),
        ) {
            items(
                items = uiState.value.players,
                key = { activePlayer ->
                    activePlayer.player.id
                }
            ) { activePlayer ->
                Row(
                    modifier = Modifier.animateItem()
                ) {
                    ActivePlayerListItem(
                        activePlayer = activePlayer,
                        onAdd = { value ->
                            activePlayer.add(value)
                            gameRoundViewModel.update()
                        },
                        modifier = modifier
                    )
                }
            }
        }
    }
}