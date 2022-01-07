package net.oddware.gamepad

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import androidx.recyclerview.widget.SortedListAdapterCallback
import net.oddware.gamepad.databinding.FragmentItemArchivedRoundBinding
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class ArchivedRoundsAdapter(
    private val clickListener: ArchivedRoundClickListener
): RecyclerView.Adapter<ArchivedRoundsAdapter.ArchivedRoundViewHolder>() {
    init {
        setHasStableIds(true)
    }

    interface ArchivedRoundClickListener {
        fun onArchivedRoundClick(archivedRoundModel: ArchivedRoundModel)
    }

    private val armList: SortedList<ArchivedRoundModel> =
        SortedList(
            ArchivedRoundModel::class.java,
            object : SortedListAdapterCallback<ArchivedRoundModel>(this) {
                override fun compare(o1: ArchivedRoundModel, o2: ArchivedRoundModel): Int {
                    return o1.compareTo(o2)
                }

                override fun areContentsTheSame(
                    oldItem: ArchivedRoundModel,
                    newItem: ArchivedRoundModel
                ): Boolean {
                    return oldItem.numPlayers == newItem.numPlayers &&
                            oldItem.roundID == newItem.roundID &&
                            oldItem.name == newItem.name &&
                            oldItem.gameID == newItem.gameID &&
                            oldItem.date == newItem.date
                }

                override fun areItemsTheSame(
                    oldItem: ArchivedRoundModel,
                    newItem: ArchivedRoundModel
                ): Boolean {
                    return oldItem == newItem
                }
            }
        )

    inner class ArchivedRoundViewHolder(var binding: FragmentItemArchivedRoundBinding):
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var arm: ArchivedRoundModel

        fun bind(archivedRoundModel: ArchivedRoundModel) {
            this.arm = archivedRoundModel

            with(binding) {
                tvArchivedItemGameDate.text = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(arm.date)
                tvArchivedItemGameName.text = arm.name
                tvArchivedItemPlayersVal.text = arm.numPlayers.toString()
            }

            binding.root.setOnClickListener {
                if (RecyclerView.NO_POSITION == adapterPosition) {
                    return@setOnClickListener
                }
                clickListener.onArchivedRoundClick(archivedRoundModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArchivedRoundViewHolder {
        val binding = FragmentItemArchivedRoundBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ArchivedRoundViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArchivedRoundViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return armList.size()
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).roundID
    }

    private fun getItem(position: Int): ArchivedRoundModel {
        return armList.get(position)
    }

    fun submitList(roundList: List<ArchivedRoundModel>) {
        Timber.d("submitList() received list of ${roundList.size} ArchivedRoundModel(s)")
        // For some reason, we need to clear the list before adding here, or we sometimes get doubles.
        // This works fine in ActiveRoundSortedAdapter without clearing first, so I suspect I have a bug
        // in the SortedList implementation in this class
        armList.clear()
        armList.addAll(roundList)
    }
}