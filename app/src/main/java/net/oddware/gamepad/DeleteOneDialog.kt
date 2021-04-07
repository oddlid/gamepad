package net.oddware.gamepad

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import timber.log.Timber

class DeleteOneDialog : DialogFragment() {
    companion object {
        const val ACTION_KEY = "net.oddware.gamepad.DeleteOneDialog.DELETE_ACTION"
        const val ACTION_VAL_OK = "net.oddware.gamepad.DeleteOneDialog.DELETE_OK"
        const val ACTION_VAL_CANCEL = "net.oddware.gamepad.DeleteOneDialog.DELETE_CANCEL"
    }

    private val args: DeleteOneDialogArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val builder = AlertDialog.Builder(activity)
        with(builder) {
            setMessage(getString(R.string.dlgDelOne_msgTxt, args.itemName))
            setPositiveButton(R.string.dlgDelOne_btnOkTxt) { dlgInterface, _ ->
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    ACTION_KEY,
                    ACTION_VAL_OK
                ) ?: run {
                    Timber.d("Did not find nav controller")
                }
                dlgInterface.dismiss()
            }
            setNegativeButton(R.string.dlgDelOne_btnCancelTxt) { dlgInterface, _ ->
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    ACTION_KEY,
                    ACTION_VAL_CANCEL
                ) ?: run {
                    Timber.d("Did not find nav controller")
                }
                dlgInterface.cancel()
            }
        }

        return builder.create()
    }
}