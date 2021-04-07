package net.oddware.gamepad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import net.oddware.gamepad.databinding.FragmentGameSelectionBinding
import timber.log.Timber

class GameSelectionFragment : Fragment() {

    private lateinit var adapter: GameListAdapter
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

        adapter = GameListAdapter(null, findNavController())

        binding.btnAddNewGame.setOnClickListener {
            val action =
                GameSelectionFragmentDirections.actionGameSelectionFragmentToEditItemFragment()
            action.loadAction = EditItemFragment.ACTION_ADD
            action.itemType = EditItemFragment.TYPE_GAME
            Navigation.findNavController(view).navigate(action)
        }

        val lloMgr = LinearLayoutManager(view.context)
        with(binding.rvGameList) {
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
            if (null != it) {
                Timber.d("Game list changed in DB...")
                //binding.rvGameList.adapter = GameListAdapter(it, findNavController())
                adapter.gameList = it
                adapter.notifyDataSetChanged()
            }
        })
    }

}