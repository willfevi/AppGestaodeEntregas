package com.example.belportas.presentation.view.tasklistscreen.datepicker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.MutableState
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar

fun showDatePicker(context: Context, selectedDate: MutableState<Long?>) {
    val datePicker =
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Selecione a data:")
            .build()

    datePicker.addOnPositiveButtonClickListener { timestamp ->
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.add(Calendar.DAY_OF_MONTH, +1)
        selectedDate.value = calendar.timeInMillis
    }

    val activity = context as? AppCompatActivity
    activity?.supportFragmentManager?.let { fragmentManager ->
        datePicker.show(fragmentManager, "DATE_PICKER")
    }
}