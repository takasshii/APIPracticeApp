package com.example.apipracticeapp.ui

import android.app.AlertDialog
import android.app.Dialog
import androidx.fragment.app.DialogFragment
import com.example.apipracticeapp.R

class ErrorDialogFragment(
    private val onDialogPositiveClick: NoticeDialogListener
) : DialogFragment() {

    interface NoticeDialogListener {
        fun positiveClick()
    }

    override fun onCreateDialog(savedInstanceState: android.os.Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity())
            .setTitle(getString(R.string.error_dialog_title))
            .setMessage(getString(R.string.error_dialog_message))
            .setPositiveButton(getString(R.string.error_dialog_positive_button)) { _, _ ->
                // 再読み込みを行う
                onDialogPositiveClick.positiveClick()
            }
            .setNegativeButton(getString(R.string.error_dialog_negative_button)) { _, _ ->
                // 何もしない
            }
            .create()
    }
}