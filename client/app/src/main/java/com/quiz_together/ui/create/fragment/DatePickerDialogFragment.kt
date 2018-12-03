package com.quiz_together.ui.create.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import java.util.*

class DatePickerDialogFragment : DialogFragment() {
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    companion object {
        @JvmStatic
        fun newInstance(listener: DatePickerDialog.OnDateSetListener): DatePickerDialogFragment {
            val datePicker = DatePickerDialogFragment()
            datePicker.dateSetListener = listener
            return datePicker
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(activity, dateSetListener, year, month, day)
    }
}