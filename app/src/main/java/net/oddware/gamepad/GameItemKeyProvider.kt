package net.oddware.gamepad

import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.widget.RecyclerView

class GameItemKeyProvider(private val rv: RecyclerView) :
    ItemKeyProvider<Long>(ItemKeyProvider.SCOPE_MAPPED) {
    override fun getKey(position: Int): Long? {
        return rv.adapter?.getItemId(position)
    }

    override fun getPosition(key: Long): Int {
        val vh = rv.findViewHolderForItemId(key)
        return vh?.layoutPosition ?: RecyclerView.NO_POSITION
    }
}