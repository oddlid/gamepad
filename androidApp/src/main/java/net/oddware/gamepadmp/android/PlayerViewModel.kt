package net.oddware.gamepadmp.android

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class PlayerListMode {
    LIST,
    ADD,
    EDIT,
}

@Immutable
sealed interface PlayerListUIState {
    data class Success(val players: List<Player>) : PlayerListUIState
    data object Error : PlayerListUIState
    data object Loading : PlayerListUIState
}

data class PlayerScreenUIState(
    val players: PlayerListUIState = PlayerListUIState.Loading,
    val mode: PlayerListMode = PlayerListMode.LIST,
    val currentPlayer: Player = Player(id = -1),
    val hasSelection: Boolean = false,
    val allSelected: Boolean = false,
)

class PlayerViewModel(private val playerRepository: PlayerRepository) : ViewModel() {
    private val currentMode = MutableStateFlow(PlayerListMode.LIST)
    private val currentPlayer = MutableStateFlow(Player(id = -1))

    val uiState: StateFlow<PlayerScreenUIState> = combine(
        playerRepository.getAllPlayersStream().asResult(),
        currentMode,
        currentPlayer,
        playerRepository.hasSelection(),
        playerRepository.allSelected(),
    ) { playerResult, mode, player, has, all ->
        val playerList: PlayerListUIState = when (playerResult) {
            is Result.Success -> PlayerListUIState.Success(playerResult.data)
            is Result.Loading -> PlayerListUIState.Loading
            is Result.Error -> PlayerListUIState.Error
        }
        PlayerScreenUIState(
            players = playerList,
            mode = mode,
            currentPlayer = player,
            hasSelection = has,
            allSelected = all,
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = PlayerScreenUIState(),
        )


    fun onCancel() {
        viewModelScope.launch {
            currentMode.emit(PlayerListMode.LIST)
        }
    }

    fun onEdit(player: Player) {
        viewModelScope.launch {
            currentPlayer.emit(player)
            currentMode.emit(PlayerListMode.EDIT)
        }
    }

    fun onAdd() {
        viewModelScope.launch {
            currentMode.emit(PlayerListMode.ADD)
        }
    }

    fun onSaveNew(name: String, uri: Uri?) {
        viewModelScope.launch {
            playerRepository.insertPlayer(Player(name = name, iconUri = uri))
            currentMode.emit(PlayerListMode.LIST)
        }
    }

    fun onUpdate(player: Player, name: String, uri: Uri?) {
        viewModelScope.launch {
            playerRepository.updatePlayer(player.copy(name = name, iconUri = uri))
            currentMode.emit(PlayerListMode.LIST)
        }
    }

    fun onDelete(player: Player) {
        viewModelScope.launch {
            playerRepository.deletePlayer(player)
        }
    }

    fun onSelect(player: Player) {
        viewModelScope.launch {
            playerRepository.toggleSelection(player.id)
        }
    }

    fun onSelectAll(selected: Boolean) {
        viewModelScope.launch {
            playerRepository.selectAll(selected)
        }
    }

    suspend fun getActivePlayers() =
        playerRepository.filterBySelection(true).map { ActivePlayer(it) }
}