package com.amm.valleytraildam.ui.view.adminview

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.amm.valleytraildam.R
import com.amm.valleytraildam.databinding.ActivityRouteDetailsBinding
import com.amm.valleytraildam.model.Route
import com.amm.valleytraildam.model.User
import com.amm.valleytraildam.ui.viewmodel.adminviewmodel.AdminUsersAdapter
import com.amm.valleytraildam.ui.viewmodel.adminviewmodel.AdminUsersViewHolder
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AdminRouteDetailsActivity : AppCompatActivity(), AdminUsersViewHolder.OnUserClickListener {
    private lateinit var binding: ActivityRouteDetailsBinding
    private lateinit var route: Route
    private var userList: MutableList<User> = mutableListOf()
    private lateinit var adapter: AdminUsersAdapter
    private var isEditing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRouteDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isActive = intent.extras?.getBoolean("isActive") == true
        Log.i("route", isActive.toString())
        val date = intent.getStringExtra("date")

        retrieveRouteInfo(date, isActive)




        if (!isActive){
            binding.editBtn.visibility = View.GONE
        }
        binding.editBtn.setOnClickListener {
            if (isEditing) {
                // Guardar cambios
                saveChanges()
            } else {
                // Habilitar edición
                enableEditing()
            }
        }

    }


    private fun enableEditing() {
        // Cambiar texto del botón
        binding.editBtn.setText(R.string.guardar)

        // Habilitar la edición de los campos
        binding.tvDate.isEnabled = true
        binding.tvName.isEnabled = true
        binding.tvTime.isEnabled = true
        binding.etMaxParticipants.isEnabled = true
        binding.etParticipants.isEnabled = true
        // Cambiar el estado de edición
        isEditing = true
    }

    private fun saveChanges() {
        // Actualizar la ruta en Firebase
        val db = FirebaseFirestore.getInstance()
        val collection = "active_routes"

        route.date?.let {
            db.collection(collection).document(it).update(
                hashMapOf(
                    "routeName" to binding.tvName.text.toString(),
                    "time" to binding.tvTime.text.toString(),
                    "maxParticipants" to binding.etMaxParticipants.text.toString().toInt(),
                    "participants" to binding.etParticipants.text.toString().toInt()
                ) as Map<String, Any>
            )
                .addOnSuccessListener {
                    // Actualización exitosa
                    isEditing = false
                    binding.editBtn.setText(R.string.editar)
                    disableEditing()
                }
                .addOnFailureListener { e ->
                    // Error al actualizar
                    Log.e(TAG, "Error al actualizar la ruta en Firebase", e)
                    Toast.makeText(this, "Error al actualizar la ruta", Toast.LENGTH_SHORT).show()
                }
        }

    }

    private fun disableEditing() {
        // Deshabilitar la edición de los campos
        binding.tvDate.isEnabled = false
        binding.tvName.isEnabled = false
        binding.tvTime.isEnabled = false
        binding.etMaxParticipants.isEnabled = false
        binding.etParticipants.isEnabled = false
    }


    private fun initRecyclerView(adminUserList: List<User>) {
        adapter = AdminUsersAdapter(adminUserList, this)
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.adapter = adapter
    }


    private fun displayRouteInfo() {

        binding.tvDate.setText(route.date)
        binding.tvName.setText(route.routeName)
        binding.tvTime.setText(route.time)
        binding.etMaxParticipants.setText( route.maxParticipants.toString())
        binding.etParticipants.setText( route.participants.toString())
    }

    private fun retrieveRouteInfo(date: String?, isActive: Boolean) {
        val collection: String = if (isActive) {
            "active_routes"
        } else {
            "archived_routes"
        }

        val db = FirebaseFirestore.getInstance()

        MainScope().launch(Dispatchers.Main) {
            route = db.collection(collection).document(date!!).get().await()
                .toObject(Route::class.java)!!

            if (
                route.users != null
            ) {

                route.users?.let { users ->
                    if (users.isNotEmpty()) {
                        for (user in users) {
                            db.collection("users").document(user).get().await()
                                .toObject(User::class.java)
                                ?.let { userList.add(it) }
                        }
                        initRecyclerView(userList)
                    }
                }
            }


            displayRouteInfo()
        }
    }


    private fun deleteUserFromRoute(email: String, routeDate: String) {
        val routeRef = FirebaseFirestore.getInstance().collection("active_routes").document(routeDate)
        routeRef.update("users", FieldValue.arrayRemove(email))
            .addOnSuccessListener {
                Log.d(TAG, "Usuario eliminado de la ruta")

            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error al eliminar usuario de la ruta", e)
            }

    }

    private fun deleteRouteFromUser(email: String, routeDate: String) {
        val userRoutesRef = FirebaseFirestore.getInstance().collection("users").document(email)
        userRoutesRef.update("currentRoutes", FieldValue.arrayRemove(routeDate))
            .addOnSuccessListener {
                Log.d(TAG, "Ruta eliminada del usuario")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error al eliminar ruta del usuario", e)
            }
    }

    override fun onUserClick(user: User) {
        val options = arrayOf("Llamar", "Enviar correo electrónico", "Eliminar usuario")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selecciona una opción")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> {
                    // Llamar
                    val phone = user.phone?.trim()
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:$phone")
                    }
                    startActivity(intent)
                }

                1 -> {
                    // Enviar correo electrónico
                    val email = user.email?.lowercase()?.trim()
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:")
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                    }
                    startActivity(intent)
                }

                2 -> {

                    Log.i("RouteDetails", user.email.toString())
                    Log.i("RouteDetails", route.toString())

                    MainScope().launch(Dispatchers.Main) {
                        deleteRouteFromUser(user.email ?: "", route.date ?: "")
                        deleteUserFromRoute( user.email ?: "", route.date ?: "")
                        adapter.notifyDataSetChanged()
                    }




                }


            }
        }
        builder.show()


    }



}