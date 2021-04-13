package net.oddware.gamepad

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import net.oddware.gamepad.databinding.FragmentPlayerSelectionBinding
import timber.log.Timber

class PlayerSelectionFragment : Fragment(), PlayerListAdapter.PlayerClickListener,
    ActionMode.Callback {

    private lateinit var adapter: PlayerListAdapter
    private var game: Game? = null
    private var actionMode: ActionMode? = null
    private var _binding: FragmentPlayerSelectionBinding? = null
    private val binding get() = _binding!!
    private val gameViewModel: GameViewModel by activityViewModels()
    private val ssvm: SavedStateViewModel by viewModels {
        SavedStateViewModelFactory(
            requireActivity().application,
            this
        )
    }
    private val args: PlayerSelectionFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerSelectionBinding.inflate(inflater, container, false)
        val view = binding.root

        adapter = PlayerListAdapter(this)

        binding.btnAddNewPlayer.setOnClickListener {
            val action =
                PlayerSelectionFragmentDirections.actionPlayerSelectionFragmentToEditItemFragment()
            action.loadAction = EditItemFragment.ACTION_ADD
            action.itemType = EditItemFragment.TYPE_PLAYER
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

        // TODO: Find a way to persist args.gameID
        //  If we come to this fragment from GameSelectionFragment, it's all good, but if we come
        //  back here from ConfirmationDialog, this argument is gone, and then "game" will be null.
        //  We can either pass on gameID to ConfirmationDialog and pass it back, or we can save it
        //  in the viewModel. Not sure which is best....
        //if (args.gameID < 0) {
        //    return
        //}
        //gameViewModel.getGame(args.gameID).observe(viewLifecycleOwner, {
        //    if (null != it) {
        //        game = it
        //    }
        //})

        //ssvm.getCurrentGameID().observe(viewLifecycleOwner, { gameID ->
        //    Timber.d("Got current gameID: $gameID")
        //    if (SavedStateViewModel.INVALID_ID != gameID) {
        //        gameViewModel.getGame(gameID).observe(viewLifecycleOwner, {
        //            if (null != it) {
        //                game = it
        //            }
        //        })
        //    }
        //})

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
                val numSelected = adapter.getBatchSelectionIds().size
                val gameName = game?.name ?: "UNDEFINED"
                val msg = "Starting game \"$gameName\" with $numSelected players..."
                Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
                return true
            }
            R.id.menu_delete_selected -> {
                val numSelected = adapter.getBatchSelectionIds().size
                val action1 =
                    PlayerSelectionFragmentDirections.actionPlayerSelectionFragmentToConfirmationDialog()
                action1.itemName = "$numSelected players"
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
        adapter.initBatchMode(false)
        actionMode = null
    }
}