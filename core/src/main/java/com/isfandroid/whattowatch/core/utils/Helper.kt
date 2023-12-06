package com.isfandroid.whattowatch.core.utils

import android.widget.Toast
import androidx.fragment.app.Fragment

object Helper {

    // SHOW TOAST FUNCTION FOR FRAGMENT
    fun Fragment.showToast(msg: String) {
        Toast.makeText(requireActivity().applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    // CONVERT MINUTES IN INTEGER TO STRING
    fun formatMinutesToHoursAndMinutes(totalMinutes: Int): String {
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60

        val formattedTime = StringBuilder()

        if (hours > 0) {
            formattedTime.append("${hours}h ")
        }

        if (minutes > 0 || hours == 0) {
            formattedTime.append("${minutes}m")
        }

        return formattedTime.toString()
    }
}