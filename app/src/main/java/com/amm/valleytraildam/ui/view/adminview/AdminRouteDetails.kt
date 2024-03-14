package com.amm.valleytraildam.ui.view.adminview

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.amm.valleytraildam.databinding.ActivityRouteDetailsBinding
import com.amm.valleytraildam.model.Route
import com.amm.valleytraildam.model.User
import com.amm.valleytraildam.ui.viewmodel.adminviewmodel.AdminHistoryAdapter
import com.amm.valleytraildam.ui.viewmodel.adminviewmodel.AdminUsersAdapter
import com.amm.valleytraildam.ui.viewmodel.adminviewmodel.AdminUsersViewHolder
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Locale

class AdminRouteDetails : AppCompatActivity(), AdminUsersViewHolder.OnUserClickListener {
    private lateinit var binding: ActivityRouteDetailsBinding
    private lateinit var route: Route
    private var userList: MutableList<User> = mutableListOf()
    private lateinit var adapter: AdminUsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRouteDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val date = intent.getStringExtra("date")
        val isActive = intent.getBooleanExtra("isActive", false)


        retrieveRouteInfo(date, isActive)


    }


    private fun initRecyclerView(adminUserList: List<User>) {
        adapter = AdminUsersAdapter(adminUserList, this)
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.adapter = adapter
    }


    private fun displayRouteInfo() {

        binding.tvDate.text = route.date
        binding.tvName.text = route.routeName
        binding.tvTime.text = route.time
        binding.tvParticipants.text = "${route.participants.toString()} participantes"
    }

    private fun retrieveRouteInfo(date: String?, isActive: Boolean) {
        val collection: String = if (isActive) {
            "active_routes"
        } else {
            "archived_routes"
        }

        val db = FirebaseFirestore.getInstance()

        GlobalScope.launch(Dispatchers.Main) {
            route = db.collection(collection).document(date!!).get().await()
                .toObject(Route::class.java)!!

            if (
                route.users != null
            ){

                Log.i("Rute dentro de bucle", route.users.toString())
                route.users?.let { users ->
                    if (users.isNotEmpty()) {
                        for (user in users) {
                            db.collection("users").document(user).get().await().toObject(User::class.java)
                                ?.let { userList.add(it) }
                        }
                        initRecyclerView(userList)
                    }
                }
            }


            displayRouteInfo()
        }
    }




    override fun onUserClick(user: User) {
        val options = arrayOf("Llamar", "Enviar correo electrónico")

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


            }
        }
        builder.show()


    }

}