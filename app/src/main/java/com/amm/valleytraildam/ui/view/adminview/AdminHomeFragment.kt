package com.amm.valleytraildam.ui.view.adminview


import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amm.valleytraildam.R
import com.amm.valleytraildam.databinding.FragmentAdminHomeBinding
import com.amm.valleytraildam.model.Route
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class AdminHomeFragment : Fragment() {
    private lateinit var binding: FragmentAdminHomeBinding
    private var date: String = ""
    private var occupiedDay = false
    private var blockedDay = false
    private var db = FirebaseFirestore.getInstance()
    private var activeRoute: Route? = null
    private val _isOccupiedDayLiveData = MutableLiveData<Boolean>()
    private val isOccupiedDayLiveData: LiveData<Boolean> = _isOccupiedDayLiveData
    private var selectedDate = ""
    private var year = 0
    private var month = 0
    private var dayOfMonth = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflar el layout para este fragmento
        binding = FragmentAdminHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Deshabilitar botones por defecto
        binding.btnDetails.isEnabled = false
        binding.blockBtn.isEnabled = false
        binding.archiveBtn.isEnabled = false
        binding.btnAddRoute.isEnabled = false

        // Manejar clic en "Añadir Ruta"
        binding.btnAddRoute.setOnClickListener {
            // Iniciar actividad de añadir ruta con la fecha seleccionada
            val intent = Intent(requireContext(), AdminAddRoute::class.java)
            intent.putExtra("date", date)
            startActivity(intent)
        }

        // Manejar selección de fecha en el calendario
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Guardar fecha seleccionada
            this.year = year
            this.month = month
            this.dayOfMonth = dayOfMonth

            // Configurar la visualización del calendario
            setupCalendar(year, month, dayOfMonth)

            // Verificar si el día seleccionado está ocupado
            isOccupiedDay()
            // Actualizar observador de día ocupado
            updateListenerOccupiedDay(year, month, dayOfMonth)
        }

        // Manejar clic en "Detalles"
        binding.btnDetails.setOnClickListener {
            // Mostrar detalles de la ruta activa para la fecha seleccionada
            val intent = Intent(requireContext(), AdminRouteDetails::class.java)
            intent.putExtra("date", activeRoute!!.date)
            intent.putExtra("isActive", true)
            startActivity(intent)
        }

        // Manejar clic en "Bloquear" o "Desbloquear"
        binding.blockBtn.setOnClickListener {
            // Bloquear o desbloquear día dependiendo del estado actual
            if (blockedDay) {
                db.collection("active_routes").document(selectedDate).delete()
            } else {
                db.collection("active_routes").document(selectedDate).set(
                    hashMapOf(
                        "date" to selectedDate,
                        "routeName" to "No disponible",
                        "max_participants" to 0,
                        "participants" to 0
                    )
                )
            }
            // Actualizar estado del día
            isOccupiedDay()
            // Configurar visualización del calendario
            setupCalendar(year, month, dayOfMonth)
        }

        // Manejar clic en "Archivar"
        binding.archiveBtn.setOnClickListener {
            // Archivar la ruta activa para la fecha seleccionada
            CoroutineScope(Dispatchers.IO).launch {
                archiveRoute(activeRoute, db)
            }
            // Actualizar estado del día
            isOccupiedDay()
            // Configurar visualización del calendario
            setupCalendar(year, month, dayOfMonth)
        }
    }

    // Función para archivar una ruta
    private fun archiveRoute(activeRoute: Route?, db: FirebaseFirestore) {
        if (activeRoute != null) {
            db.collection("archived_routes").document(selectedDate).set(
                hashMapOf(
                    "date" to activeRoute.date,
                    "routeName" to activeRoute.routeName,
                    "time" to activeRoute.time,
                    "participants" to activeRoute.participants,
                    "isActive" to false,
                    "users" to activeRoute.users
                )
            ).addOnSuccessListener {
                db.collection("active_routes").document(selectedDate).delete()
                Toast.makeText(activity, "Ruta archivada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función para actualizar el observador de día ocupado
    private fun updateListenerOccupiedDay(year: Int, month: Int, dayOfMonth: Int) {
        val fixedMonth = month + 1
        selectedDate = "$dayOfMonth-$fixedMonth-$year"

        isOccupiedDayLiveData.observe(viewLifecycleOwner) { occupiedDay ->
            if (occupiedDay) {
                // Configurar botones cuando el día está ocupado
                binding.blockBtn.isEnabled = false
                binding.archiveBtn.isEnabled = true
                binding.blockBtn.text = getString(R.string.ya_hay_una_ruta)
                binding.btnDetails.isEnabled = true
                binding.btnAddRoute.isEnabled = false
            } else {
                // Configurar botones cuando el día no está ocupado
                binding.btnDetails.isEnabled = false
                binding.archiveBtn.isEnabled = false
                binding.blockBtn.isEnabled = true
                if (blockedDay) {
                    // Configurar botón de bloqueo cuando el día está bloqueado
                    binding.blockBtn.text = getString(R.string.desbloquear)
                    binding.btnAddRoute.isEnabled = false
                } else {
                    // Configurar botón de bloqueo cuando el día no está bloqueado
                    binding.blockBtn.text = getString(R.string.bloquear)
                    binding.btnAddRoute.isEnabled = true
                }
            }
        }
    }

    // Función para verificar si el día está ocupado
    private fun isOccupiedDay() {
        CoroutineScope(Dispatchers.IO).launch {
            val routeRef = db.collection("active_routes")
            val documentSnapshot = routeRef.document(date).get().await()

            // Verificar si existe una ruta para la fecha dada
            val route = documentSnapshot.toObject<Route>()
            activeRoute = route

            if (activeRoute?.routeName == "No disponible") {
                blockedDay = true
                occupiedDay = false
            } else {
                occupiedDay = route != null
                blockedDay = false
            }

            // Notificar el cambio usando LiveData en el hilo principal
            withContext(Dispatchers.Main) {
                _isOccupiedDayLiveData.value = occupiedDay
            }
        }
    }

    // Función para configurar la visualización del calendario
    private fun setupCalendar(year: Int, month: Int, dayOfMonth: Int) {
        getDayOfWeek(year, month, dayOfMonth)

        // Formatear la fecha
        val month = month + 1
        date = "$dayOfMonth-$month-$year"

        // Obtener el estado del día desde la base de datos y actualizar la visualización en función del estado
        CoroutineScope(Dispatchers.IO).launch {
            db.collection("active_routes").document(date).get()
                .addOnSuccessListener { documentSnapshot ->
                    val activeRoute: Route? = documentSnapshot.toObject(Route::class.java)
                    if (activeRoute != null) {
                        if (activeRoute.routeName == "No disponible") {
                            // Ruta no disponible (día bloqueado)
                            updateBlockedView(activeRoute)
                        } else {
                            // Ruta disponible (día ocupado)
                            updateOccupiedView(activeRoute)
                        }
                    } else {
                        // Día libre (sin ruta)
                        updateFreeView()
                    }
                }.addOnFailureListener {
                    Log.e("Exception", "Error en la llamada: $it")
                }
        }
    }

    // Función para obtener el día de la semana
    private fun getDayOfWeek(year: Int, month: Int, dayOfMonth: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(dayOfMonth, month, year)
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    // Función para actualizar la visualización cuando el día está bloqueado
    private fun updateBlockedView(activeRoute: Route) {
        binding.tvParticipants.visibility = View.GONE
        binding.tvRouteName.visibility = View.GONE
        binding.tvTime.visibility = View.GONE
        binding.tvDate.text = activeRoute.date
        binding.tvTime.text = activeRoute.time
        binding.tvDayStatus.text = getString(R.string.bloqueado)
        binding.tvDayStatus.setBackgroundColor(Color.RED)
    }

    // Función para actualizar la visualización cuando el día está ocupado
    private fun updateOccupiedView(activeRoute: Route) {
        binding.tvParticipants.visibility = View.VISIBLE
        binding.tvRouteName.visibility = View.VISIBLE
        binding.tvParticipants.visibility = View.VISIBLE
        binding.tvParticipants.text = activeRoute.participants!!.toString()
        binding.tvDate.text = date
        binding.tvRouteName.text = activeRoute.routeName.toString()
        binding.tvTime.text = activeRoute.time
        binding.tvDayStatus.text = getString(R.string.ruta_agendada)
        binding.tvDayStatus.setBackgroundColor(Color.YELLOW)
    }

    // Función para actualizar la visualización cuando el día está libre
    private fun updateFreeView() {
        binding.tvParticipants.visibility = View.GONE
        binding.tvRouteName.visibility = View.GONE
        binding.tvTime.visibility = View.GONE
        binding.tvDate.text = date
        binding.tvDayStatus.text = getString(R.string.libre)
        binding.tvDayStatus.setBackgroundColor(Color.GREEN)
    }


}