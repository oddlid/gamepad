package net.oddware.gamepad

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import net.oddware.gamepad.databinding.FragmentItemGameSelectionBinding

class GameListAdapter(
    var gameList: List<Game>? = null,
    var tracker: SelectionTracker<Long>? = null,
    private val navCtl: NavController
) : RecyclerView.Adapter<GameListAdapter.GameViewHolder>() {
    init {
        setHasStableIds(true)
    }

    inner class GameViewHolder(var binding: FragmentItemGameSelectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Game, isActivated: Boolean) {
            binding.root.isActivated = isActivated

            binding.tvGameItemName.text = game.name

            binding.btnEditGameItem.setOnClickListener {
                val action =
                    GameSelectionFragmentDirections.actionGameSelectionFragmentToEditItemFragment()
                action.itemType = EditItemFragment.TYPE_GAME
                action.loadAction = EditItemFragment.ACTION_EDIT
                action.itemID = game.gameID
                navCtl.navigate(action)
            }
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long? = itemId
            }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = FragmentItemGameSelectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = gameList?.get(position) ?: return
        tracker?.let {
            holder.bind(game, it.isSelected(position.toLong()))
        }
    }

    override fun getItemCount(): Int {
        return gameList?.size ?: 0
    }

    override fun getItemId(position: Int): Long {
        val game = gameList?.get(position) ?: return -1L
        return game.gameID
    }
}