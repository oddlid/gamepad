package net.oddware.gamepadmp.android

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
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
    val players: PlayerListUIState,
    val mode: PlayerListMode,
    val currentPlayer: Player,
    val isError: Boolean,
    val isLoading: Boolean,
    val hasSelection: Boolean,
    val allSelected: Boolean,
)

class PlayerViewModel(private val playerRepository: PlayerRepository) : ViewModel() {
    private val players: Flow<Result<List<Player>>> =
        playerRepository.getAllPlayersStream().asResult()
    private val isError = MutableStateFlow(false)
    private val isLoading = MutableStateFlow(false)
    private val hasSelection = MutableStateFlow(false)
    private val allSelected = MutableStateFlow(false)
    private val currentMode = MutableStateFlow(PlayerListMode.LIST)
    private val currentPlayer = MutableStateFlow(Player(id = -1))
    private val _selectedPlayers = mutableListOf<Player>()

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch {
            isError.emit(true)
        }
    }

    val uiState: StateFlow<PlayerScreenUIState> = combine(
        players,
        isError,
        isLoading,
        currentMode,
        currentPlayer,
    ) { playerResult, error, loading, mode, player ->
        val playerList: PlayerListUIState = when (playerResult) {
            is Result.Success -> PlayerListUIState.Success(playerResult.data)
            is Result.Loading -> PlayerListUIState.Loading
            is Result.Error -> PlayerListUIState.Error
        }
        PlayerScreenUIState(
            players = playerList,
            mode = mode,
            currentPlayer = player,
            isError = error,
            isLoading = loading,
            hasSelection = false,
            allSelected = false,
        )
    }
        .combine(
            hasSelection
        ) { self, has ->
            self.copy(hasSelection = has)
        }
        .combine(
            allSelected
        ) { self, all ->
            self.copy(allSelected = all)
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = PlayerScreenUIState(
                players = PlayerListUIState.Loading,
                mode = PlayerListMode.LIST,
                currentPlayer = Player(id = -1),
                isError = false,
                isLoading = false,
                hasSelection = false,
                allSelected = false,
            )
        )


    fun onCancel() {
        viewModelScope.launch(exceptionHandler) {
            currentMode.emit(PlayerListMode.LIST)
        }
    }

    fun onEdit(player: Player) {
        viewModelScope.launch(exceptionHandler) {
            currentPlayer.emit(player)
            currentMode.emit(PlayerListMode.EDIT)
        }
    }

    fun onAdd() {
        viewModelScope.launch(exceptionHandler) {
            currentMode.emit(PlayerListMode.ADD)
        }
    }

    fun onSaveNew(name: String) {
        viewModelScope.launch(exceptionHandler) {
            playerRepository.insertPlayer(Player(name = name))
            currentMode.emit(PlayerListMode.LIST)
        }
    }

    fun onUpdate(player: Player, name: String) {
        viewModelScope.launch(exceptionHandler) {
            playerRepository.updatePlayer(player.copy(name = name))
            currentMode.emit(PlayerListMode.LIST)
        }
    }

    fun onDelete(player: Player) {
        viewModelScope.launch(exceptionHandler) {
            playerRepository.deletePlayer(player)
            syncSelectedPlayers()
        }
    }

    fun onSelect(player: Player) {
        viewModelScope.launch(exceptionHandler) {
            playerRepository.toggleSelection(player.id)
            syncSelectedPlayers()
            playerRepository.hasSelection().collect {
                hasSelection.emit(it)
            }
            playerRepository.allSelected().collect {
                allSelected.emit(it)
            }
        }
    }

    fun onSelectAll(selected: Boolean) {
        viewModelScope.launch(exceptionHandler) {
            playerRepository.selectAll(selected)
            syncSelectedPlayers()
            allSelected.emit(selected)
            hasSelection.emit(selected)
        }
    }

    private suspend fun syncSelectedPlayers() {
        _selectedPlayers.apply {
            clear()
            addAll(playerRepository.filterBySelection(true))
        }
    }

    fun getActivePlayers(): List<ActivePlayer> = _selectedPlayers.map { ActivePlayer(it) }


}