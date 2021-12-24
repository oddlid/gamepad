package net.oddware.gamepad

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import net.oddware.gamepad.databinding.FragmentPlayerSelectionBinding
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class PlayerSelectionFragment : Fragment(), PlayerListAdapter.PlayerClickListener,
    ActionMode.Callback {

    private lateinit var adapter: PlayerListAdapter
    private var game: Game? = null
    private var actionMode: ActionMode? = null
    private var _binding: FragmentPlayerSelectionBinding? = null
    private val binding get() = _binding!!
    private val gameViewModel: GameViewModel by activityViewModels()
    private val ssvm: SavedStateViewModel by activityViewModels() // changing from viewModels() to activityViewModels() made all the difference!
    //private val args: PlayerSelectionFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerSelectionBinding.inflate(inflater, container, false)
        val view = binding.root

        adapter = PlayerListAdapter(this)

        binding.btnAddNewPlayer.setOnClickListener {
            val action =
                PlayerSelectionFragmentDirections.actionPlayerSelectionFragmentToEditItemFragment()
            action.loadAction = EditItemFragment.ACTION_ADD
            action.itemType = EditItemFragment.TYPE_PLAYER
            context?.let {
                action.mode = it.getString(R.string.fragEditItem_mode_addPlayer)
            }
            Navigation.findNavController(view).navigate(action)
        }

        val lloMgr = LinearLayoutManager(view.context)
        with(binding.rvPlayerList) {
            addItemDecoration(DividerItemDecoration(view.context, lloMgr.orientation))
            setHasFixedSize(true)
            layoutManager = lloMgr
            adapter = this@PlayerSelectionFragment.adapter
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameViewModel.players.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

        MainActivity.hideKeyboard(requireContext(), view)

        val gameID = ssvm.getCurrentGameID()
        if (null == gameID) {
            Timber.d("Got null for gameID")
            return
        }
        gameViewModel.getGame(gameID).observe(viewLifecycleOwner, {
            if (null != it) {
                game = it
            }
        })
    }

    override fun onPlayerClick(player: Player) {
        if (null == actionMode) {
            Snackbar.make(binding.root, "Long click to start selection", Snackbar.LENGTH_SHORT)
                .show()
            return
        }
        adapter.togglePlayerInBatchSet(player)
        if (adapter.getBatchSelectionIds().isEmpty()) {
            actionMode?.finish()
        } else {
            actionMode?.title = adapter.getBatchSelectionIds().size.toString()
        }
    }

    override fun onPlayerLongClick(player: Player): Boolean {
        if (null != actionMode) {
            onPlayerClick(player)
            return true
        }
        adapter.initBatchMode(true)
        adapter.togglePlayerInBatchSet(player)

        actionMode = activity?.startActionMode(this)

        return true
    }

    override fun onPlayerEditClick(player: Player) {
        val action =
            PlayerSelectionFragmentDirections.actionPlayerSelectionFragmentToEditItemFragment()
        with(action) {
            itemType = EditItemFragment.TYPE_PLAYER
            loadAction = EditItemFragment.ACTION_EDIT
            itemID = player.playerID
        }
        context?.let {
            action.mode = it.getString(R.string.fragEditItem_mode_editPlayer)
        }
        findNavController().navigate(action)
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.let {
            it.menuInflater.inflate(R.menu.player_list_batch, menu)
            it.title = "1"
        }
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return false
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (null == item) {
            return false
        }

        when (item.itemId) {
            R.id.menu_start_game -> {
                //val numSelected = adapter.getBatchSelectionIds().size
                //val gameName = game?.name ?: "UNDEFINED"
                //val msg = "Starting game \"$gameName\" with $numSelected players..."
                //Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
                Timber.d("Start game clicked")
                startRound()
                return true
            }
            R.id.menu_delete_selected -> {
                val numSelected = adapter.getBatchSelectionIds().size
                val action1 =
                    PlayerSelectionFragmentDirections.actionPlayerSelectionFragmentToConfirmationDialog()
                //action1.itemName = "$numSelected players"
                action1.numItems = numSelected
                action1.itemType =
                    context?.getString(R.string.dlgConfirm_type_players) ?: "UNDEFINED"
                val navCtl = findNavController()

                // Important to set up listening BEFORE navigate()!
                navCtl.currentBackStackEntry?.savedStateHandle?.let { ssh ->
                    ssh.getLiveData<String>(ConfirmationDialog.ACTION_KEY)
                        .observe(viewLifecycleOwner, {
                            if (ConfirmationDialog.ACTION_VAL_OK == it) {
                                // delete
                                val selectedPlayers = adapter.getBatchSelection().toTypedArray()
                                gameViewModel.deletePlayers(*selectedPlayers)
                                // close actionMode
                                actionMode?.finish()
                                actionMode = null
                                // navigate back
                                val action2 =
                                    ConfirmationDialogDirections.actionConfirmationDialogToPlayerSelectionFragment()
                                navCtl.navigate(action2)
                            }
                            ssh.remove<String>(ConfirmationDialog.ACTION_KEY)
                        })
                }

                navCtl.navigate(action1)

                return true
            }
            R.id.menu_select_all -> {
                adapter.selectAll()
                actionMode?.title = adapter.getBatchSelectionIds().size.toString()
                return true
            }
        }

        return false
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        Timber.d("inside onDestroyActionMode()")
        adapter.initBatchMode(false)
        actionMode = null
    }

    private fun startRound() {
        // First, create new round and save to DB
        // Then, get the round from DB in order to get it's autogenerated ID
        // Then, for each selected player, create a Point instance where:
        // - roundID points to the Round we just got
        // - playerID points to the Player
        // - gameID points to the current Game
        // - value is 0
        // Now, through this list of Points, Game, Round and Players are linked
        // Save this list of Points to DB
        // Save roundID and GameID to SavedStateViewModel
        // If we now launch ActiveRoundFragment, it should be able to get all info it needs from roundID + gameID
        Timber.d("In startRound()...")

        val gameID = game?.gameID ?: -1L
        if (-1L == gameID) {
            Timber.d("Got invalid gameID, not starting round")
            return
        }

        gameViewModel.addRound(Round(gameID = gameID, date = Date()))
            .observe(viewLifecycleOwner, { done ->
                if (!done) {
                    Timber.d("Got (impossible) false from addRound, returning")
                    return@observe
                }

                gameViewModel.getLastInsertedActiveRound()
                    .observe(viewLifecycleOwner, { lastInsertedActiveRound ->
                        if (null == lastInsertedActiveRound) {
                            Timber.d("Passed round == null, returning")
                            return@observe
                        }

                        Timber.d("Passed round ID: ${lastInsertedActiveRound.roundID}")

                        val points: MutableList<Point> = mutableListOf()
                        for (p in adapter.getBatchSelection()) {
                            points.add(
                                Point(
                                    roundID = lastInsertedActiveRound.roundID,
                                    gameID = gameID,
                                    playerID = p.playerID
                                )
                            )
                        }
                        gameViewModel.addPoints(*points.toTypedArray())
                        ssvm.setCurrentGameID(gameID)
                        ssvm.setCurrentRoundID(lastInsertedActiveRound.roundID)

                        actionMode?.finish()
                        actionMode = null
                        // launch...
                        val action =
                            PlayerSelectionFragmentDirections.actionPlayerSelectionFragmentToActiveRoundFragment()
                        game?.let { g -> action.gameName = g.name }
                        lastInsertedActiveRound.date?.let { dt ->
                            action.roundDate =
                                SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(dt)
                        }
                        Timber.d("Launching round...")
                        findNavController().navigate(action)
                    })
            })

        // There's a bug at this step!
        // If the app is first run, then this works as intended, but if you play one round and finish,
        // so you get back here, and start a new round, then lastInsertedActiveRound returns the
        // previous round, which causes lots of problems, and may lead to crash.
        // It seems the result of lastInsertedActiveRound is cached in some way.
        // Update:
        // After refactoring gameDao.getLastInsertedActiveRound to not return livedata directly, but
        // rather do that myself in GameViewModel, we no longer crash, but most times when there is
        // no round with finished == 0 in the DB when we get here, then nothing happens on the first
        // "start game" click, since we then reach the null branch below. Then on the next click,
        // we do get a valid round passed, but it's ID is one behind, and there are now 2 rows of
        // rounds in the DB marked as unfinished.
        // So what seems to happen, is that since we use coroutines, the addRound call above is not
        // finished committing to the DB when we get to the observe below.
        // Update:
        // Seems I solved it now with adding an observer on addRound and nesting the observe on last
        // inserted active round into this again.


    }
}