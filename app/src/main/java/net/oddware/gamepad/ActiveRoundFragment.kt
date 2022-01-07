package net.oddware.gamepad

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
    private val args: ActiveRoundFragmentArgs by navArgs()
    private var archiveMode: Boolean = false
    private var currentGame: Game? = null
    private var currentRound: Round? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActiveRoundBinding.inflate(inflater, container, false)
        val view = binding.root

        archiveMode = args.archiveMode
        adapter = ActiveRoundSortedAdapter(this, archiveMode)


        val lloMgr = LinearLayoutManager(view.context)
        with(binding.rvActiveRoundPlayerList) {
            addItemDecoration(DividerItemDecoration(view.context, lloMgr.orientation))
            setHasFixedSize(true)
            layoutManager = lloMgr
            adapter = this@ActiveRoundFragment.adapter
        }

        if (!archiveMode) {
            with(binding){
                btnResumeRound.visibility = View.GONE
                btnDeleteRound.visibility = View.GONE
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        setHasOptionsMenu(true)

//        requireActivity()
//            .onBackPressedDispatcher
//            .addCallback(this) {
//                this.isEnabled = true
//                Timber.d("Hooked back pressed from fragment")
//            }
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
                return@observe
            }
        })

        if (archiveMode) {
            binding.btnDeleteRound.setOnClickListener {
                currentRound?.let { cr ->
                    findNavController().navigateUp()
                    gameViewModel.deleteRounds(cr)
                    ssvm.clearCurrentGameID()
                    ssvm.clearCurrentRoundID()
                }
            }
            binding.btnResumeRound.setOnClickListener {
                currentRound?.let { cr ->
                    cr.finished = false
                    gameViewModel.updateRound(cr)
                    binding.btnDeleteRound.visibility = View.GONE
                    binding.btnResumeRound.visibility = View.GONE
                    archiveMode = false
                    adapter.archiveMode = archiveMode
                    binding.rvActiveRoundPlayerList.adapter = adapter
                }
            }
        }

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
    //  NOT if one presses device button back...
    // I might go back to having a button to finish the game, but if so, then the app should check
    // on startup if there are unfinished rounds, and if so, open the most recent.
    // I'm not quite sure yet what I think is best; to automatically finish a round when the user leaves
    // this fragment, or demand they finish manually, with the possibility to come back and finish later.
    // For the use cases I imagine for this app, it's unlikely that the user leaves the app for so long
    // that the app is started fresh, and the round gone. But if so, the round should be automatically
    // finished as fragment onDetach() is reached.
    // We could then rather have an option in the archive list to resume/restart a round.
    private fun finishRound() {
        ssvm.clearCurrentGameID()
        ssvm.clearCurrentRoundID()
        if (archiveMode) {
            Timber.d("Skipping updating round, as archive_mode is true")
            return
        }
        currentRound?.let {
            it.finished = true
            gameViewModel.updateRound(it)
        } ?: run {
            Timber.d("finishRound(): no current round to save")
        }
    }
}