package com.amm.valleytraildam.ui.view.adminview


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
import com.amm.valleytraildam.databinding.FragmentAdminHomeBinding
import com.amm.valleytraildam.model.Route
import com.amm.valleytraildam.model.User
import com.google.firebase.auth.FirebaseAuth
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            this.year = year
            this.month = month
            this.dayOfMonth = dayOfMonth

            setupCalendar(year, month, dayOfMonth)
            isOccupiedDay()
            updateListenerOccupiedDay(year, month, dayOfMonth)

        }


        // Lógica para bloquear o desbloquear el día
        binding.blockBtn.setOnClickListener {
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

            setupCalendar(year, month, dayOfMonth)
        }

        binding.archiveBtn.setOnClickListener {

            archiveRoute(activeRoute, db)
            Log.i("ArchivedRoute", "$activeRoute")
            setupCalendar(year, month, dayOfMonth)

        }


    }

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


    private fun updateListenerOccupiedDay(year: Int, month: Int, dayOfMonth: Int) {
        val fixedMonth = month + 1
        selectedDate = "$dayOfMonth-$fixedMonth-$year"

        isOccupiedDayLiveData.observe(viewLifecycleOwner) { occuppiedDay ->
            if (occuppiedDay) {
                // Día ocupado: deshabilitar botón de bloqueo
                binding.blockBtn.isEnabled = false
                binding.archiveBtn.isEnabled = true
                binding.blockBtn.text = "Ya hay una ruta"
            } else {
                // Día sin ocupar
                binding.archiveBtn.isEnabled = false
                binding.blockBtn.isEnabled = true
                if (blockedDay) {
                    // Día no ocupado pero bloqueado: mostrar texto "Desbloquear"
                    binding.blockBtn.text = "Desbloquear $dayOfMonth-$month-$year"
                } else {
                    // Día no ocupado y no bloqueado: mostrar texto "Bloquear"
                    binding.blockBtn.text = "Bloquear $dayOfMonth-$month-$year"
                }
            }
        }
    }

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

    private fun setupCalendar(year: Int, month: Int, dayOfMonth: Int) {
        getDayOfWeek(year, month, dayOfMonth)
        Log.e("YourFragment", "$dayOfMonth-$month-$year")

        val month = month + 1
        date = "$dayOfMonth-$month-$year"

        CoroutineScope(Dispatchers.IO).launch {
            val userDocRef = db.collection("users")
                .document(FirebaseAuth.getInstance().currentUser!!.email.toString())
            val documentSnapshot = userDocRef.get().await()
            val user = documentSnapshot.toObject<User>()!!
            val normalizedEmail = user.email.toString()

            db.collection("active_routes").document(date).get()
                .addOnSuccessListener { documentSnapshot ->
                    val activeRoute: Route? = documentSnapshot.toObject(Route::class.java)
                    Log.e("Info DB", "ActiveRoute data: $activeRoute")

                    if (activeRoute != null) {
                        if (activeRoute.routeName == "No disponible") {
                            // Ruta no nula y maxParticipants igual a 0: lógica de ruta bloqueada
                            updateBlockedView(activeRoute)
                        } else {
                            // Ruta no nula y maxParticipants no igual a 0: lógica para ruta ocupada
                            updateOccupiedView(activeRoute)
                        }
                    } else {
                        // Ruta nula: lógica para día libre
                        updateFreeView()
                    }
                }.addOnFailureListener {
                    Log.e("Exception", "Error en la llamada: $it")
                }
        }
    }

    private fun getDayOfWeek(year: Int, month: Int, dayOfMonth: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(dayOfMonth, month, year)
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    private fun updateBlockedView(activeRoute: Route) {
        binding.tvParticipants.visibility = View.GONE
        binding.tvRouteName.visibility = View.GONE
        binding.tvTime.visibility = View.GONE
        binding.tvDate.text = activeRoute.date
        binding.tvTime.text = activeRoute.time
        binding.tvDayStatus.text = "Bloqueado"
        binding.tvDayStatus.setBackgroundColor(Color.RED)
    }

    private fun updateOccupiedView(activeRoute: Route) {
        binding.tvParticipants.visibility = View.VISIBLE
        binding.tvRouteName.visibility = View.VISIBLE
        binding.tvParticipants.visibility = View.VISIBLE
        binding.tvParticipants.text = activeRoute.participants!!.toString()
        binding.tvDate.text = date
        binding.tvRouteName.text = activeRoute.routeName.toString()
        binding.tvTime.text = activeRoute.time
        binding.tvDayStatus.text = "Ocupado"
        binding.tvDayStatus.setBackgroundColor(Color.YELLOW)
    }

    private fun updateFreeView() {
        binding.tvParticipants.visibility = View.GONE
        binding.tvRouteName.visibility = View.GONE
        binding.tvTime.visibility = View.GONE
        binding.tvDate.text = date
        binding.tvDayStatus.text = "Libre"
        binding.tvDayStatus.setBackgroundColor(Color.GREEN)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = AdminHomeFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }
}