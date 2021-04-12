package net.oddware.gamepad

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class GameItemDetailsLookup(private val rv: RecyclerView) : ItemDetailsLookup<Long>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = rv.findChildViewUnder(e.x, e.y)
        if (null != view) {
            val vh = rv.getChildViewHolder(view) as GameListAdapter.GameViewHolder
            return vh.getItemDetails()
        }
        Timber.d("Unable to find item at touch position")
        return null
    }
}