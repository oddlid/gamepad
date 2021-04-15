package net.oddware.gamepad

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import net.oddware.gamepad.databinding.FragmentGameSelectionBinding
import timber.log.Timber

class GameSelectionFragment : Fragment(), GameListAdapter.GameClickListener, ActionMode.Callback {

    private lateinit var adapter: GameListAdapter
    private var actionMode: ActionMode? = null
    private var _binding: FragmentGameSelectionBinding? = null
    private val binding get() = _binding!!
    private val gameViewModel: GameViewModel by activityViewModels()
    private val ssvm: SavedStateViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameSelectionBinding.inflate(inflater, container, false)
        val view = binding.root

        Timber.d("Loading GameSelectionFragment...")

        adapter = GameListAdapter(this)

        binding.btnAddNewGame.setOnClickListener {
            val action =
                GameSelectionFragmentDirections.actionGameSelectionFragmentToEditItemFragment()
            action.loadAction = EditItemFragment.ACTION_ADD
            action.itemType = EditItemFragment.TYPE_GAME
            context?.let {
                action.mode = it.getString(R.string.fragEditItem_mode_addGame)
            }
            Navigation.findNavController(view).navigate(action)
        }

        val lloMgr = LinearLayoutManager(view.context)
        with(binding.rvGameList) {
            addItemDecoration(DividerItemDecoration(view.context, lloMgr.orientation))
            setHasFixedSize(true)
            layoutManager = lloMgr
            adapter = this@GameSelectionFragment.adapter
        }


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameViewModel.games.observe(viewLifecycleOwner, {
            //if (null != it) {
            //    Timber.d("Game list changed in DB...")
            //    adapter.gameList = it
            //    adapter.notifyDataSetChanged()
            //}
            Timber.d("Submitting new game list to adapter...")
            adapter.submitList(it)
            //adapter.notifyDataSetChanged()
        })

        MainActivity.hideKeyboard(requireContext(), view)
    }

    override fun onGameClick(game: Game) {
        if (null == actionMode) {
            Timber.d("Should load player selection fragment")
            val action =
                GameSelectionFragmentDirections.actionGameSelectionFragmentToPlayerSelectionFragment()
            action.gameName = game.name
            //action.gameID = game.gameID // this might be obsolete with ssvm
            ssvm.setCurrentGameID(game.gameID)
            findNavController().navigate(action)
            return
        }
        adapter.toggleGameInBatchSet(game)
        if (adapter.getBatchSelectionIds().isEmpty()) {
            actionMode?.finish()
        } else {
            actionMode?.title = adapter.getBatchSelectionIds().size.toString()
        }
    }

    override fun onGameLongClick(game: Game): Boolean {
        if (null != actionMode) {
            onGameClick(game)
            return true
        }
        adapter.initBatchMode(true)
        adapter.toggleGameInBatchSet(game)

        actionMode = activity?.startActionMode(this)

        return true
    }

    override fun onGameEditClick(game: Game) {
        val action = GameSelectionFragmentDirections.actionGameSelectionFragmentToEditItemFragment()
        action.itemType = EditItemFragment.TYPE_GAME
        action.loadAction = EditItemFragment.ACTION_EDIT
        action.itemID = game.gameID
        context?.let {
            action.mode = it.getString(R.string.fragEditItem_mode_editGame)
        }
        findNavController().navigate(action)
    }

    //override fun onGameDeleteClick(game: Game) {
    //}

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.let {
            it.menuInflater.inflate(R.menu.game_list_batch, menu)
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
            R.id.menu_select_all -> {
                Timber.d("Menu item select_all clicked")
                adapter.selectAll()
                actionMode?.title = adapter.getBatchSelectionIds().size.toString()
                return true
            }
            R.id.menu_delete_selected -> {
                Timber.d("Menu item delete_selected clicked")

                val numSelected = adapter.getBatchSelectionIds().size
                val action1 =
                    GameSelectionFragmentDirections.actionGameSelectionFragmentToConfirmationDialog()
                //action1.itemName = "$numSelected games"
                action1.numItems = numSelected
                action1.itemType = context?.getString(R.string.dlgConfirm_type_games) ?: "UNDEFINED"
                val navCtl = findNavController()

                // Important to set up listening BEFORE navigate()!
                navCtl.currentBackStackEntry?.savedStateHandle?.let { ssh ->
                    ssh.getLiveData<String>(ConfirmationDialog.ACTION_KEY)
                        .observe(viewLifecycleOwner, {
                            if (ConfirmationDialog.ACTION_VAL_OK == it) {
                                // delete
                                val selectedGames = adapter.getBatchSelection().toTypedArray()
                                gameViewModel.deleteGames(*selectedGames)
                                // close actionMode
                                actionMode?.finish()
                                actionMode = null
                                // navigate back
                                val action2 =
                                    ConfirmationDialogDirections.actionConfirmationDialogToGameSelectionFragment()
                                navCtl.navigate(action2)
                            }
                            ssh.remove<String>(ConfirmationDialog.ACTION_KEY)
                        })
                }

                navCtl.navigate(action1)

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