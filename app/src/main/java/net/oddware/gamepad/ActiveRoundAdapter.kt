package net.oddware.gamepad

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import net.oddware.gamepad.databinding.FragmentItemActivePlayerBinding

class ActiveRoundAdapter(
    private val pointUpdateListener: PointUpdateListener
) :
    ListAdapter<ActivePlayerModel, ActiveRoundAdapter.ActiveRoundViewHolder>(
        DIFF_CALLBACK
    ) {

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<ActivePlayerModel> =
            object : DiffUtil.ItemCallback<ActivePlayerModel>() {
                override fun areItemsTheSame(
                    oldItem: ActivePlayerModel,
                    newItem: ActivePlayerModel
                ): Boolean {
                    return oldItem.player.playerID == newItem.player.playerID &&
                            oldItem.game.gameID == newItem.game.gameID &&
                            oldItem.round.roundID == newItem.round.roundID
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

            }
    }

    init {
        setHasStableIds(true)
    }

    inner class ActiveRoundViewHolder(var binding: FragmentItemActivePlayerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var apm: ActivePlayerModel

        fun bind(activePlayerModel: ActivePlayerModel) {
            apm = activePlayerModel

            with(binding) {
                tvActivePlayerItemName.text = activePlayerModel.player.name
                tvActivePlayerItemPoints.text = activePlayerModel.getTotalPoints().toString()
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
                notifyItemChanged(adapterPosition)
                //notifyDataSetChanged()
                //reorderList()
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
        val apm = getItem(position) ?: return
        holder.bind(apm)
    }

    override fun getItemId(position: Int): Long {
        val item = getItem(position) ?: return RecyclerView.NO_ID
        return item.player.playerID
    }

    //fun reorderList() {
    //    val tmpList = currentList.toMutableList()
    //    tmpList.sort()
    //    submitList(tmpList)
    //    //notifyDataSetChanged()
    //}
}