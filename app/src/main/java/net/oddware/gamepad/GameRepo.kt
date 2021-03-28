package net.oddware.gamepad

import android.content.Context

class GameRepo(private val gameDao: GameDao) {
    companion object {
        @Volatile
        private var INSTANCE: GameRepo? = null

        fun getInstance(ctx: Context): GameRepo {
            val tmpInst = INSTANCE
            if (null != tmpInst) {
                return tmpInst
            }
            synchronized(this) {
                val inst = GameRepo(GameDB.get(ctx).gameDao())
                INSTANCE = inst
                return inst
            }
        }
    }

    // ...
}