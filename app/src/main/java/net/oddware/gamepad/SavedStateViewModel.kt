package net.oddware.gamepad

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import timber.log.Timber

/*
This ViewModel class is for stuff that we don't need to have in our DB, just related to UI.
It's very important to use "by activityViewModels()" when using this class, or it won't work
at all! Despite docs saying you should use "by viewModels()"...
 */
class SavedStateViewModel(private val state: SavedStateHandle) : ViewModel() {
    companion object {
        const val INVALID_ID = -1L
        private const val KEY_GAME_ID = "net.oddware.gamepad.ssvm.GAME_ID"
    }

    fun setCurrentGameID(id: Long) {
        Timber.d("Setting current gameID to: $id")
        state.set(KEY_GAME_ID, id)
    }

    //fun getCurrentGameID(): LiveData<Long> = state.getLiveData(KEY_GAME_ID)
    fun getCurrentGameID(): Long? = state.get<Long>(KEY_GAME_ID)
}