package com.quiz_together.ui.create.fragment

import android.app.Dialog
import android.app.DialogFragment
import android.app.FragmentManager
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import java.util.*

class TimePickerDialogFragment : DialogFragment() {
    private lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener

    companion object {
        @JvmStatic
        fun show(listener: TimePickerDialog.OnTimeSetListener, fm: FragmentManager) {
            val timePicker = TimePickerDialogFragment()
            timePicker.timeSetListener = listener
            timePicker.show(fm, "예약 시간")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(activity, timeSetListener, hour, minute,
                DateFormat.is24HourFormat(activity))
    }
}
