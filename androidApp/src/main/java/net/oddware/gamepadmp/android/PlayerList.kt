package net.oddware.gamepadmp.android

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

enum class PlayerListMode {
    LIST,
    ADD,
    EDIT,
}

@Composable
fun PlayerListScreen(
    modifier: Modifier = Modifier,
    playerViewModel: PlayerViewModel = viewModel(),
) {
    var mode by rememberSaveable { mutableStateOf(PlayerListMode.LIST) }
}