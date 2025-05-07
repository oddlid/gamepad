package net.oddware.gamepadmp.android

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class GameListMode {
    LIST,
    ADD,
    EDIT,
}

@Immutable
sealed interface GameListUiState {
    data class Success(val games: List<Game>) : GameListUiState
    data object Error : GameListUiState
    data object Loading : GameListUiState
}

data class GameScreenUIState(
    val games: GameListUiState,
    val mode: GameListMode,
    val currentGame: Game,
    val isError: Boolean,
    val isLoading: Boolean,
)

class GameViewModel(private val gamesRepository: GamesRepository) : ViewModel() {
    private val isError = MutableStateFlow(false)
    private val isLoading = MutableStateFlow(false)
    private val currentMode = MutableStateFlow(GameListMode.LIST)
    private val currentGame = MutableStateFlow(Game(id = -1))

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch {
            isError.emit(true)
        }
    }

    val uiState: StateFlow<GameScreenUIState> = combine(
        gamesRepository.getAllGamesStream().asResult(),
        currentMode,
        currentGame,
        isError,
        isLoading,
    ) { gamesResult, mode, game, errorOccurred, loading ->
        val gameList: GameListUiState = when (gamesResult) {
            is Result.Success -> GameListUiState.Success(gamesResult.data)
            is Result.Loading -> GameListUiState.Loading
            is Result.Error -> GameListUiState.Error
        }

        GameScreenUIState(
            games = gameList,
            mode = mode,
            currentGame = game,
            isError = errorOccurred,
            isLoading = loading,
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = GameScreenUIState(
                games = GameListUiState.Loading,
                mode = GameListMode.LIST,
                currentGame = Game(id = -1),
                isError = false,
                isLoading = true,
            )
        )

    fun onEdit(game: Game) {
        viewModelScope.launch(exceptionHandler) {
            currentGame.emit(game)
            currentMode.emit(GameListMode.EDIT)
        }
    }

    fun onCancel() {
        viewModelScope.launch(exceptionHandler) {
            currentMode.emit(GameListMode.LIST)
        }
    }

    fun onAdd() {
        viewModelScope.launch(exceptionHandler) {
            currentMode.emit(GameListMode.ADD)
        }
    }

    fun onSaveNew(name: String) {
        viewModelScope.launch(exceptionHandler) {
            gamesRepository.insertGame(Game(name = name))
            currentMode.emit(GameListMode.LIST)
        }
    }

    fun onUpdate(game: Game, name: String) {
        viewModelScope.launch(exceptionHandler) {
            gamesRepository.updateGame(game.copy(name = name))
            currentMode.emit(GameListMode.LIST)
        }
    }

    fun onDelete(game: Game) {
        viewModelScope.launch(exceptionHandler) {
            gamesRepository.deleteGame(game)
        }
    }

    fun onSelect(game: Game) {
        viewModelScope.launch(exceptionHandler) {
            currentGame.emit(game)
        }
    }
}