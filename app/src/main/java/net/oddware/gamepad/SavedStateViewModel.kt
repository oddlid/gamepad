package net.oddware.gamepad

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import timber.log.Timber

class SavedStateViewModel(private val state: SavedStateHandle) : ViewModel() {
    companion object {
        const val INVALID_ID = -1L
        private const val KEY_GAME_ID = "net.oddware.gamepad.ssvm.GAME_ID"
    }

    fun setCurrentGameID(id: Long) {
        Timber.d("Setting current gameID to: $id")
        state.set(KEY_GAME_ID, id)
    }

    // TODO: find out why none of these two returns a value after set
    //fun getCurrentGameID(): LiveData<Long> = state.getLiveData(KEY_GAME_ID)
    fun getCurrentGameID(): Long? = state.get<Long>(KEY_GAME_ID)
}