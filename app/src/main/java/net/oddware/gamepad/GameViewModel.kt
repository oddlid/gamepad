package net.oddware.gamepad

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class GameViewModel(app: Application) : AndroidViewModel(app) {
    private val gameRepo: GameRepo = GameRepo.getInstance(app)

    // ...
}