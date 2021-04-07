package net.oddware.gamepad

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import net.oddware.gamepad.databinding.FragmentItemGameSelectionBinding

class GameListAdapter(
    var gameList: List<Game>? = null,
    private val navCtl: NavController
) : RecyclerView.Adapter<GameListAdapter.GameViewHolder>() {
    inner class GameViewHolder(var binding: FragmentItemGameSelectionBinding) :
        RecyclerView.ViewHolder(binding.root)


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
        holder.binding.tvGameItemName.text = game.name

        holder.binding.btnEditGameItem.setOnClickListener {
            val action =
                GameSelectionFragmentDirections.actionGameSelectionFragmentToEditItemFragment()
            action.itemType = EditItemFragment.TYPE_GAME
            action.loadAction = EditItemFragment.ACTION_EDIT
            action.itemID = game.gameID
            navCtl.navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return gameList?.size ?: 0
    }
}