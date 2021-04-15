package net.oddware.gamepad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import net.oddware.gamepad.databinding.FragmentActiveRoundBinding
import timber.log.Timber

class ActiveRoundFragment : Fragment(), ActiveRoundSortedAdapter.PointUpdateListener {

    private lateinit var adapter: ActiveRoundSortedAdapter
    private var _binding: FragmentActiveRoundBinding? = null
    private val binding get() = _binding!!
    private val gameViewModel: GameViewModel by activityViewModels()
    private val ssvm: SavedStateViewModel by activityViewModels() // changing from viewModels() to activityViewModels() made all the difference!

    private var currentGame: Game? = null
    private var currentRound: Round? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActiveRoundBinding.inflate(inflater, container, false)
        val view = binding.root

        //adapter = ActiveRoundAdapter(this)
        adapter = ActiveRoundSortedAdapter(this)

        // Moving this to onDetach instead
        //binding.btnActiveRoundFinishGame.setOnClickListener {
        //    currentRound?.let {
        //        it.finished = true
        //        gameViewModel.updateRound(it)
        //    }
        //    ssvm.clearCurrentGameID()
        //    ssvm.clearCurrentRoundID()
        //    val action =
        //        ActiveRoundFragmentDirections.actionActiveRoundFragmentToGameSelectionFragment()
        //    Navigation.findNavController(view).navigate(action)
        //}

        val lloMgr = LinearLayoutManager(view.context)
        with(binding.rvActiveRoundPlayerList) {
            addItemDecoration(DividerItemDecoration(view.context, lloMgr.orientation))
            setHasFixedSize(true)
            layoutManager = lloMgr
            adapter = this@ActiveRoundFragment.adapter
        }

        //

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        Timber.d("Finishing game round...")
        finishRound()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // observe viewmodels

        val gameID = ssvm.getCurrentGameID()
        if (null == gameID) {
            Timber.d("Got null for gameID, unable to continue")
            return
        }
        gameViewModel.getGame(gameID).observe(viewLifecycleOwner, {
            if (null != it) {
                currentGame = it
                //binding.tvActiveRoundHdrGameName.text = it.name
                return@observe
            }
        })
        val roundID = ssvm.getCurrentRoundID()
        if (null == roundID) {
            Timber.d("Got null for roundID, unable to continue")
            return
        }
        gameViewModel.getRound(roundID).observe(viewLifecycleOwner, {
            if (null != it) {
                currentRound = it
                //binding.tvActiveRoundHdrDate.text = it.date.toString()
                return@observe
            }
        })

        gameViewModel.getActivePlayerModelsForRound(gameID, roundID).observe(viewLifecycleOwner, {
            Timber.d("Passing list of ActivePlayerModel to ActiveRoundSortedAdapter...")
            adapter.submitList(it)
        })
    }

    override fun onPointUpdated(point: Point) {
        gameViewModel.addPoint(point)
    }

    // TODO: Find out a bombproof way to call this method in all cases where the user goes back
    //  Currently, when I call this from onDetach(), it works if the user presses toolbar back, but
    // NOT if one presses device button back...
    private fun finishRound() {
        currentRound?.let {
            it.finished = true
            gameViewModel.updateRound(it)
        } ?: run {
            Timber.d("finishRound(): no current round to save")
        }
        ssvm.clearCurrentGameID()
        ssvm.clearCurrentRoundID()
    }
}