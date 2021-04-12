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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameSelectionBinding.inflate(inflater, container, false)
        val view = binding.root

        Timber.d("Loading GameSelectionFragment...")

        adapter = GameListAdapter(this)

        binding.btnAddNewGame.setOnClickListener {
            val action =
                GameSelectionFragmentDirections.actionGameSelectionFragmentToEditItemFragment()
            action.loadAction = EditItemFragment.ACTION_ADD
            action.itemType = EditItemFragment.TYPE_GAME
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
            adapter.submitList(it.toMutableList())
            //adapter.notifyDataSetChanged()
        })
    }

    override fun onGameClick(game: Game) {
        if (null == actionMode) {
            Timber.d("actionMode is null")
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
                // TODO: Implement confirmation dialog
                val selectedGames = adapter.getBatchSelection().toTypedArray()
                gameViewModel.deleteGames(*selectedGames)
                actionMode?.finish()
                actionMode = null
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