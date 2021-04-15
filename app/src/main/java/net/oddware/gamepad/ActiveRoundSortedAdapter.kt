package net.oddware.gamepad

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import androidx.recyclerview.widget.SortedListAdapterCallback
import com.google.android.material.snackbar.Snackbar
import net.oddware.gamepad.databinding.FragmentItemActivePlayerBinding

class ActiveRoundSortedAdapter(
    private val pointUpdateListener: PointUpdateListener
) : RecyclerView.Adapter<ActiveRoundSortedAdapter.ActiveRoundViewHolder>() {

    private val apmList: SortedList<ActivePlayerModel> =
        SortedList(
            ActivePlayerModel::class.java,
            object : SortedListAdapterCallback<ActivePlayerModel>(this) {
                override fun compare(o1: ActivePlayerModel, o2: ActivePlayerModel): Int {
                    return o1.compareTo(o2)
                }

                override fun areContentsTheSame(
                    oldItem: ActivePlayerModel,
                    newItem: ActivePlayerModel
                ): Boolean {
                    return oldItem.player.playerID == newItem.player.playerID &&
                            oldItem.game.gameID == newItem.game.gameID &&
                            oldItem.round.roundID == newItem.round.roundID &&
                            oldItem.getTotalPoints() == newItem.getTotalPoints()
                }

                override fun areItemsTheSame(
                    oldItem: ActivePlayerModel,
                    newItem: ActivePlayerModel
                ): Boolean {
                    return oldItem.player == newItem.player &&
                            oldItem.game == newItem.game &&
                            oldItem.round == newItem.round &&
                            oldItem.point == newItem.point
                }

            })

    inner class ActiveRoundViewHolder(var binding: FragmentItemActivePlayerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var apm: ActivePlayerModel

        fun bind(activePlayerModel: ActivePlayerModel) {
            this.apm = activePlayerModel

            with(binding) {
                tvActivePlayerItemName.text = apm.player.name
                tvActivePlayerItemPoints.text = apm.getTotalPoints().toString()
            }

            binding.btnActivePlayerUpdatePoints.setOnClickListener {
                val pTxt = binding.etActivePlayerItemUpdatePoints.text.toString().trim()
                val points = pTxt.toLongOrNull()
                if (null == points) {
                    Snackbar.make(binding.root, "Invalid value: \"$pTxt\"", Snackbar.LENGTH_SHORT)
                        .show()
                    binding.etActivePlayerItemUpdatePoints.text = null
                    binding.etActivePlayerItemUpdatePoints.requestFocus()
                    return@setOnClickListener
                }
                val newPoint = apm.updatePoints(points)
                pointUpdateListener.onPointUpdated(newPoint)
                binding.etActivePlayerItemUpdatePoints.text = null

                // This call makes the difference of showing or not the point update.
                // But still, no reordering happens...
                //notifyItemChanged(adapterPosition)

                // Well, just this one call made it all work perfectly :)
                apmList.updateItemAt(adapterPosition, apm)
            }
        }
    }

    interface PointUpdateListener {
        fun onPointUpdated(point: Point)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveRoundViewHolder {
        val binding = FragmentItemActivePlayerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ActiveRoundViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActiveRoundViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return apmList.size()
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).player.playerID
    }

    fun getItem(position: Int): ActivePlayerModel {
        return apmList.get(position)
    }

    fun clear() = apmList.clear()

    fun submitList(playerList: List<ActivePlayerModel>) {
        apmList.addAll(playerList)
    }
}