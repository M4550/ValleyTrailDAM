package com.amm.valleytraildam.ui.view.adminview

import android.app.TimePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.amm.valleytraildam.R
import com.amm.valleytraildam.databinding.ActivityAdminAddRouteBinding
import com.amm.valleytraildam.ui.viewmodel.adminviewmodel.AdminCreateRoute
import java.util.Calendar

class AdminAddRoute : AppCompatActivity() {

    private lateinit var binding: ActivityAdminAddRouteBinding
    private var participantNumber = 0
    private lateinit var date: String
    private var selectedTime = ""
    private var routeKey = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAddRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.let {
            date = it.getString("date").toString()
        }

        binding.tvDate.text = date
        binding.seekBar.min = 1
        binding.seekBar.max = 16
        binding.seekBar.progress = 0

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                progress + 1
                val selectedValue: String = progress.toString()
                binding.tvSelectedParticipants.text = selectedValue
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                participantNumber = seekBar!!.progress
            }
        })

        binding.btnTimePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this, { _, hourOfDay, minute ->
                    selectedTime = String.format("%02d:%02d Horas", hourOfDay, minute)
                    Toast.makeText(this, selectedTime, Toast.LENGTH_SHORT).show()
                    binding.tvTime.text = selectedTime

                }, hour, minute, true
            )
            timePickerDialog.show()
        }

        binding.rgRouteKey.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbCong -> {
                    routeKey = "A través del Congosto"
                }

                R.id.rbAnc -> {
                    routeKey = "Caminos Ancestrales"
                }

                R.id.rbCiv -> {
                    routeKey = "Civilizaciones Pasadas"
                }
            }

            Log.i("RouteKey", routeKey)
        }

        binding.btnCreateRoute.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.apply {
                setTitle("Confirmación")
                setMessage("¿Los datos de la ruta son correctos?")
                setPositiveButton("Sí") { _, _ ->
                    if (participantNumber != 0 && selectedTime.isNotBlank() && routeKey.isNotBlank()) {
                        AdminCreateRoute.AdminCreateRoute(
                            date, participantNumber, routeKey, selectedTime, this@AdminAddRoute
                        )
                    } else {
                        Toast.makeText(
                            this@AdminAddRoute,
                            "Todos los campos deben estar cubiertos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                create().show()
            }
        }
    }
}



