package com.amm.valleytraildam.ui.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.amm.valleytraildam.R
import com.amm.valleytraildam.databinding.ActivityRequestRouteBinding
import com.amm.valleytraildam.model.model.Route
import com.amm.valleytraildam.model.model.User
import com.amm.valleytraildam.ui.viewmodel.CheckRouteAvailability
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class RequestRouteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRequestRouteBinding
    private var participantNumber: Int = 1

    private var isAvailable: Boolean = false
    private lateinit var user: User
    private var normalizedEmail = ""
    private var dateMaxParticipants = 10


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityRequestRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setRouteTitle()

        binding.seekBar.min = 1
        binding.seekBar.max = 12

        val customDialog = CustomDialog(this)

        customDialog.show()

        binding.calendarView.minDate = System.currentTimeMillis()

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->

            binding.seekBar.progress = 0

            getDayOfWeek(year, month, dayOfMonth)

            Log.e("RequestRouteActivity", "$dayOfMonth-$month-$year")


            val month = month + 1

            val db = FirebaseFirestore.getInstance()

            val date = "$dayOfMonth-$month-$year"


            CoroutineScope(Dispatchers.IO).launch {

                val userDocRef = db.collection("users")
                    .document(FirebaseAuth.getInstance().currentUser!!.email.toString())
                val documentSnapshot = userDocRef.get().await()
                user = documentSnapshot.toObject<User>()!!
                normalizedEmail = user.email.toString()

            }

            db.collection("active_routes").document(date).get()
                .addOnSuccessListener { documentSnapshot ->

                    val activeRoute: Route? = documentSnapshot.toObject(Route::class.java)
                    Log.e("Info DB", "ActiveRoute data: $activeRoute")

                    binding.tvAvailability.text = getString(R.string.disponible)
                    binding.seekBar.visibility = View.VISIBLE
                    binding.tvAvailability.setBackgroundColor(Color.GREEN)
                    binding.btnSave.visibility = View.VISIBLE

                    if (activeRoute?.date != null) {


                        binding.btnSave.visibility = View.GONE
                        binding.tvAvailability.text = getString(R.string.ruta_llena)
                        binding.seekBar.visibility = View.GONE
                        binding.tvAvailability.setBackgroundColor(Color.RED)

                        if (activeRoute.users!!.contains(normalizedEmail)) {
                            binding.btnSave.visibility = View.GONE
                            binding.tvAvailability.text =
                                getString(R.string.ya_est_s_inscrito_en_esta_ruta)
                            binding.seekBar.visibility = View.GONE
                            binding.tvAvailability.setBackgroundColor(Color.LTGRAY)
                        } else {
                            val freePlaces =
                                activeRoute.maxParticipants!! - activeRoute.participants!!
                            binding.tvAvailability.text = "Ruta con $freePlaces plazas libres"
                            binding.tvAvailability.setBackgroundColor(Color.YELLOW)
                            binding.btnSave.visibility = View.VISIBLE
                            dateMaxParticipants = activeRoute.maxParticipants!!
                            binding.seekBar.visibility = View.VISIBLE
                            binding.seekBar.max = freePlaces
                        }


                    }


                }.addOnFailureListener {
                    Exception("Error getting documents: ", it)
                }


        }






        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Actualizar el texto con el valor seleccionado
                progress + 1
                val selectedValue: String = progress.toString()
                binding.tvSelectedParticipants.text = selectedValue
                Log.i("TAG", "Selected value: $selectedValue")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                participantNumber = seekBar!!.progress
                Log.i("TAG", "Participant number: $participantNumber")
            }
        })
    }

    private fun setRouteTitle() {

        intent.extras?.let {
            Log.i("RouteTitle", it.getString("routeKey").toString())

            when (it.getString("routeKey")) {
                "anc" -> {
                    binding.tvTitle.setText(R.string.caminos_ancestrales)
                }

                "cong" -> {
                    binding.tvTitle.setText(R.string.a_traves_del_congosto)
                }

                "civ" -> {
                    binding.tvTitle.setText(R.string.civilizaciones_pasadas)
                }


            }


        }
    }

    private fun getDayOfWeek(year: Int, month: Int, dayOfMonth: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(dayOfMonth, month, year)
        return calendar.get(Calendar.DAY_OF_WEEK)

    }


    class CustomDialog(context: Context) {

        private val dialog: AlertDialog = AlertDialog.Builder(context).create()

        init {
            // Establecer el título y el mensaje del diálogo
            dialog.setTitle("Aceptacion de Requisitos")
            dialog.setMessage(
                "Al hacer clic en Aceptar, " + "consiente recibir un correo electrónico con la información de la ruta " + "y sus condiciones de contratación."
            )

            // Configurar el botón de cierre o de confirmación
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar") { _, _ ->

            }

            // Configurar el botón de rechazar
            dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Rechazar") { _, _ ->
                val intent = Intent(context, UserHomeActivity::class.java)
                context.startActivity(intent)
            }

            // Evitar que el diálogo se cierre al hacer clic fuera de él
            dialog.setCanceledOnTouchOutside(false)


        }

        fun show() {
            dialog.show()
        }
    }


}