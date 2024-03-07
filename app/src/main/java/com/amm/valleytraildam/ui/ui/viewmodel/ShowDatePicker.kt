package com.amm.valleytraildam.ui.ui.viewmodel

import android.app.DatePickerDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.View
import android.widget.Toast
import java.util.Calendar
import java.util.Locale




class ShowDatePicker {

    companion object {
        fun showDatePicker(context: Context, view: View, onDateSelected: (String) -> Unit) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                context,
                { _, year, monthOfYear, dayOfMonth ->
                    val bornDate = Calendar.getInstance()
                    bornDate.set(year, monthOfYear, dayOfMonth)
                    val formattedDate =
                        SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(bornDate.time)

                    onDateSelected.invoke(formattedDate)

                    HideKeyboard()
                    context?.let { HideKeyboard.hideKeyboard(view) }
                    Toast.makeText(
                        context,
                        "Fecha seleccionada: $formattedDate",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                year,
                month,
                day
            )

            datePickerDialog.show()
        }
    }
}