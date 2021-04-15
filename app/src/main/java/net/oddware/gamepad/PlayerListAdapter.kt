package net.oddware.gamepad

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.oddware.gamepad.databinding.FragmentItemPlayerSelectionBinding
import java.util.*
import kotlin.collections.LinkedHashMap

class PlayerListAdapter(
    private val clickListener: PlayerClickListener
) : ListAdapter<Player, PlayerListAdapter.PlayerViewHolder>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Player> =
            object : DiffUtil.ItemCallback<Player>() {
                override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
                    return oldItem.playerID == newItem.playerID
                }

                override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
                    return oldItem == newItem
                }

            }
    }

    init {
        setHasStableIds(true)
    }

    interface PlayerClickListener {
        fun onPlayerClick(player: Player)
        fun onPlayerLongClick(player: Player): Boolean
        fun onPlayerEditClick(player: Player)
    }

    inner class PlayerViewHolder(var binding: FragmentItemPlayerSelectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var currentPlayer: Player

        fun bind(player: Player) {
            bind(player, false)
        }

        private fun bind(player: Player, selected: Boolean) {
            currentPlayer = player
            binding.tvPlayerItemName.text = player.name

            binding.btnEditPlayerItem.setOnClickListener {
                if (RecyclerView.NO_POSITION == adapterPosition) {
                    return@setOnClickListener
                }
                clickListener.onPlayerEditClick(player)
            }

            binding.root.setOnClickListener {
                if (RecyclerView.NO_POSITION == adapterPosition) {
                    return@setOnClickListener
                }
                clickListener.onPlayerClick(player)
            }

            binding.root.setOnLongClickListener {
                if (RecyclerView.NO_POSITION == adapterPosition) {
                    return@setOnLongClickListener false
                }
                return@setOnLongClickListener clickListener.onPlayerLongClick(player)
            }

            setBatchMode(selected)
        }

        fun setBatchMode(mode: Boolean) {
            binding.root.isActivated = mode && batchSet.containsKey(currentPlayer.playerID)
        }
    }

    private enum class Payload {
        NO_PAYLOAD,
        SELECTION
    }

    private val batchSet = Collections.synchronizedMap(LinkedHashMap<Long, Player>())
    private var batchMode = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = FragmentItemPlayerSelectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val player = getItem(position) ?: return
        holder.bind(player)
    }

    override fun onBindViewHolder(
        holder: PlayerViewHolder,
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
                    holder.setBatchMode(batchMode)
                }
            }
        }
    }

    override fun getItemId(position: Int): Long {
        val player = getItem(position) ?: return RecyclerView.NO_ID
        return player.playerID
    }

    fun togglePlayerInBatchSet(player: Player) {
        if (batchSet.containsKey(player.playerID)) {
            batchSet.remove(player.playerID)
        } else if (player.playerID != -1L) {
            batchSet[player.playerID] = player
        }
        notifyItemRangeChanged(0, itemCount, Payload.SELECTION)
    }

    fun getBatchSelection() = batchSet.values

    fun getBatchSelectionIds(): Set<Long> = batchSet.keys

    fun selectAll() {
        for (i in 0 until itemCount) {
            val player = getItem(i)
            if (null != player && player.playerID >= 0) {
                batchSet[player.playerID] = player
            }
        }
        notifyItemRangeChanged(0, itemCount, Payload.SELECTION)
    }

    private fun unselectAll() {
        batchSet.clear()
        notifyItemRangeChanged(0, itemCount, Payload.SELECTION)
    }

    fun initBatchMode(mode: Boolean) {
        this.batchMode = mode
        unselectAll()
    }
}