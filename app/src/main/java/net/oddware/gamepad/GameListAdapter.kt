package net.oddware.gamepad

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.oddware.gamepad.databinding.FragmentItemGameSelectionBinding
import java.util.*
import kotlin.collections.LinkedHashMap

class GameListAdapter(
    private val clickListener: GameClickListener
) : ListAdapter<Game, GameListAdapter.GameViewHolder>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Game> =
            object : DiffUtil.ItemCallback<Game>() {
                override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
                    return oldItem.gameID == newItem.gameID
                }

                override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
                    return oldItem == newItem
                }

            }
    }

    init {
        setHasStableIds(true)
    }

    interface GameClickListener {
        fun onGameClick(game: Game)
        fun onGameLongClick(game: Game): Boolean
        fun onGameEditClick(game: Game)
    }

    inner class GameViewHolder(var binding: FragmentItemGameSelectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var currentGame: Game

        fun bind(game: Game) {
            bind(game, false)
        }

        private fun bind(game: Game, selected: Boolean) {
            // It might be a better idea to get the current game at the moment of click via
            // getItem(adapterPosition) instead, but we'll try like this first
            currentGame = game

            binding.tvGameItemName.text = game.name

            binding.btnEditGameItem.setOnClickListener {
                val pos = adapterPosition
                if (RecyclerView.NO_POSITION == pos) {
                    return@setOnClickListener
                }
                clickListener.onGameEditClick(game)
            }

            binding.root.setOnClickListener {
                val pos = adapterPosition
                if (RecyclerView.NO_POSITION == pos) {
                    return@setOnClickListener
                }
                clickListener.onGameClick(game)
            }

            binding.root.setOnLongClickListener {
                val pos = adapterPosition
                if (RecyclerView.NO_POSITION == pos) {
                    return@setOnLongClickListener false
                }
                return@setOnLongClickListener clickListener.onGameLongClick(game)
            }

            setBatchMode(selected)
        }

        fun setBatchMode(mode: Boolean) {
            //this.batchMode = mode
            //val inBatchSet = batchSet.containsKey(currentGame.gameID)
            //val selected = mode && inBatchSet
            //Timber.d("BatchMode: $mode, inBatchSet: $inBatchSet, selected: $selected")
            //binding.root.isSelected = selected // this caused med some confusion, as it was isActivated that was correct to use, not isSelected
            binding.root.isActivated = mode && batchSet.containsKey(currentGame.gameID)
        }

    }

    private enum class Payload {
        NO_PAYLOAD,
        SELECTION
    }

    private val batchSet = Collections.synchronizedMap(LinkedHashMap<Long, Game>())
    private var batchMode = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = FragmentItemGameSelectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = getItem(position) ?: return
        holder.bind(game)
    }

    override fun onBindViewHolder(
        holder: GameViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
            return
        }
        for (pl in payloads) {
            if (pl is Payload) {
                if (pl == Payload.SELECTION) {
                    // set batch mode for item
                    holder.setBatchMode(batchMode)
                }
            }
        }
    }

    override fun getItemId(position: Int): Long {
        val game = getItem(position) ?: return RecyclerView.NO_ID
        return game.gameID
    }

    fun toggleGameInBatchSet(game: Game) {
        if (batchSet.containsKey(game.gameID)) {
            batchSet.remove(game.gameID)
        } else if (game.gameID != -1L) {
            batchSet[game.gameID] = game
        }
        notifyItemRangeChanged(0, itemCount, Payload.SELECTION)
    }

    fun getBatchSelection() = batchSet.values

    fun getBatchSelectionIds(): Set<Long> = batchSet.keys

    private fun unselectAll() {
        batchSet.clear()
        notifyItemRangeChanged(0, itemCount, Payload.SELECTION)
    }

    fun initBatchMode(mode: Boolean) {
        this.batchMode = mode
        unselectAll()
    }

    fun selectAll() {
        for (i in 0 until itemCount) {
            val game = getItem(i)
            if (null != game && game.gameID >= 0) {
                batchSet[game.gameID] = game
            }
        }
        notifyItemRangeChanged(0, itemCount, Payload.SELECTION)
    }
}