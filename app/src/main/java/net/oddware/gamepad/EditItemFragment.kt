package net.oddware.gamepad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import net.oddware.gamepad.databinding.FragmentEditItemBinding
import timber.log.Timber

class EditItemFragment : Fragment() {
    companion object {
        const val ACTION_ADD = 0xADD
        const val ACTION_EDIT = 0xED1
        const val TYPE_GAME = "net.oddware.gampepad.type.GAME"
        const val TYPE_PLAYER = "net.oddware.gampepad.type.PLAYER"
    }

    private var _binding: FragmentEditItemBinding? = null
    private val binding get() = _binding!!

    private var game: Game? = null
    private var player: Player? = null

    private val args: EditItemFragmentArgs by navArgs()
    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditItemBinding.inflate(inflater, container, false)
        val view = binding.root

        // "add new after save" makes no sense if we are editing an existing item
        if (ACTION_EDIT == args.loadAction) {
            binding.chkAddNewAfterSave.visibility = View.GONE
        }

        // Set header based on action and type
        context?.let { ctx ->
            binding.tvHdrEdit.text = when (args.loadAction) {
                ACTION_ADD -> {
                    when (args.itemType) {
                        TYPE_GAME -> ctx.getString(R.string.tvHdrEdit_addGameTxt)
                        TYPE_PLAYER -> ctx.getString(R.string.tvHdrEdit_addPlayerTxt)
                        else -> ctx.getString(R.string.tvHdrEdit_txt)
                    }
                }
                ACTION_EDIT -> {
                    when (args.itemType) {
                        TYPE_GAME -> ctx.getString(R.string.tvHdrEdit_editGameTxt)
                        TYPE_PLAYER -> ctx.getString(R.string.tvHdrEdit_editPlayerTxt)
                        else -> ctx.getString(R.string.tvHdrEdit_txt)
                    }
                }
                else -> ctx.getString(R.string.tvHdrEdit_txt)
            }
        }

        if (ACTION_ADD == args.loadAction) {
            if (TYPE_GAME == args.itemType) {
                binding.btnSaveItem.setOnClickListener {
                    val txt = binding.etEditItemName.text.toString().trim()
                    if (txt.isEmpty()) {
                        Timber.d("Refusing to save with empty name")
                        Snackbar.make(view, R.string.errNoGameName, Snackbar.LENGTH_LONG).show()
                        //Toast.makeText(context, R.string.errNoGameName, Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }
                    gameViewModel.addGame(Game(name = txt))
                    if (!binding.chkAddNewAfterSave.isChecked) {
                        // navigate back
                        //val action = EditItemFragmentDirections.actionEditItemFragmentToGameSelectionFragment()
                        //Navigation.findNavController(view).navigate(action)
                        Navigation.findNavController(view)
                            .navigateUp() // just go back, as we don't know where we came from
                        // Return, or we'll get the Snackbar after getting back to the Game list
                        return@setOnClickListener
                    }
                    // Notify
                    val msg = view.context.getString(R.string.snackGameAdded_txt, txt)
                    Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
                    // clear, for continuing adding
                    binding.etEditItemName.text = null
                }
            }
        }

        if (ACTION_EDIT == args.loadAction) {
            if (TYPE_GAME == args.itemType) {
                binding.btnSaveItem.setOnClickListener {
                    val txt = binding.etEditItemName.text.toString().trim()
                    if (txt.isEmpty()) {
                        Timber.d("Refusing to save with empty name")
                        Snackbar.make(view, R.string.errNoGameName, Snackbar.LENGTH_LONG).show()
                        //Toast.makeText(context, R.string.errNoGameName, Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }
                    game?.let {
                        it.name = txt
                        Timber.d("Updating game...")
                        gameViewModel.updateGame(it)
                        Navigation.findNavController(view).navigateUp()
                    }
                }
            }
        }

        //if (args.loadAction == ACTION_EDIT) {
        //    if (args.itemType == TYPE_GAME) {
        //
        //    }
        //}

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ACTION_EDIT == args.loadAction) {
            if (TYPE_GAME == args.itemType && args.itemID > -1) {
                gameViewModel.getGame(args.itemID).observe(viewLifecycleOwner, {
                    if (null != it) {
                        game = it
                        binding.etEditItemName.setText(it.name)
                    }
                })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}