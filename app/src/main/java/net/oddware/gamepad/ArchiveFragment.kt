package net.oddware.gamepad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import net.oddware.gamepad.databinding.FragmentArchiveBinding
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class ArchiveFragment : Fragment(), ArchivedRoundsAdapter.ArchivedRoundClickListener {
    private lateinit var adapter: ArchivedRoundsAdapter
    private var _binding: FragmentArchiveBinding? = null
    private val binding get() = _binding!!
    //private var actionMode: ActionMode? = null
    private val gameViewModel: GameViewModel by activityViewModels()
    private val ssvm: SavedStateViewModel by activityViewModels() // changing from viewModels() to activityViewModels() made all the difference!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArchiveBinding.inflate(inflater, container, false)
        val view = binding.root

        adapter = ArchivedRoundsAdapter(this)

        val lloMgr = LinearLayoutManager(view.context)
        with (binding.rvArchivedRounds) {
            addItemDecoration(DividerItemDecoration(view.context, lloMgr.orientation))
            setHasFixedSize(true)
            layoutManager = lloMgr
            adapter = this@ArchiveFragment.adapter
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainActivity.hideKeyboard(requireContext(), view)

        gameViewModel.archivedRounds.observe(viewLifecycleOwner, {
            Timber.d("List of archived rounds has ${it.size} elements")
            adapter.submitList(it)
        })
    }

    override fun onArchivedRoundClick(arm: ArchivedRoundModel) {
        Timber.d("Clicked on: $arm")
        ssvm.setCurrentGameID(arm.gameID)
        ssvm.setCurrentRoundID(arm.roundID)
        val action = ArchiveFragmentDirections.actionArchiveFragmentToActiveRoundFragment()
        action.archiveMode = true
        action.gameName = arm.name
        action.roundDate = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(arm.date)
        findNavController().navigate(action)
    }
}