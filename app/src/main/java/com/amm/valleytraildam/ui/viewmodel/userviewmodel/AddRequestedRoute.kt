package com.amm.valleytraildam.ui.viewmodel.userviewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.amm.valleytraildam.ui.view.userview.UserHomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class AddRequestedRoute {
    companion object{

        fun addRequestedRoute(usersNumber: Int, date: String, context: Context, routeName: String) {


            val db = FirebaseFirestore.getInstance()
            val user = FirebaseAuth.getInstance().currentUser
            val userEmail = user?.email
            val time = "10:00h"

            db.collection("active_routes").document(date).get()
                .addOnSuccessListener { documentSnapshot ->
                    val routeRef = db.collection("active_routes").document(date)

                    if (documentSnapshot.exists()) {
                        val maxParticipants = documentSnapshot.getLong("max_participants") ?: 0
                        val currentParticipants = documentSnapshot.getLong("participants") ?: 0
                        val availableSlots = maxParticipants - currentParticipants

                        if (usersNumber <= availableSlots) {
                            routeRef.update("users", FieldValue.arrayUnion("$userEmail"))
                            val updatedParticipants = currentParticipants + usersNumber
                            routeRef.update("participants", updatedParticipants)
                            Toast.makeText(
                                context,
                                "Apuntado correctamente a la ruta",
                                Toast.LENGTH_SHORT
                            ).show()

                            if (userEmail != null) {
                                addRouteToUser(userEmail, date, db)

                                val intent = Intent(context, UserHomeActivity::class.java)
                                context.startActivity(intent)

                                Log.d(
                                    "RequestRouteActivity",
                                    "Se ha añadido la nueva ruta existente al usuario $userEmail"
                                )
                            } else {
                                Toast.makeText(
                                    context,
                                    "Se ha producido un error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "No hay suficientes plazas disponibles en esta ruta",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {

                            db.collection("active_routes").document(date).set(
                                hashMapOf(
                                    "routeName" to routeName,
                                    "date" to date,
                                    "time" to "10:00 Horas",
                                    "participants" to usersNumber,
                                    "max_participants" to 12,
                                    "users" to arrayListOf(userEmail.toString()),
                                    "isActive" to true,
                                    "time" to time,
                                    "isBloqued" to false

                                )
                            )
                            Toast.makeText(
                                context,
                                "Se ha añadido la nueva ruta",
                                Toast.LENGTH_SHORT
                            ).show()
                            if (userEmail != null) {
                                addRouteToUser(userEmail, date, db)

                                val intent = Intent(context, UserHomeActivity::class.java)
                                context.startActivity(intent)

                                Log.i(
                                    "RequestRouteActivity",
                                    "Se ha añadido la nueva ruta al usuario $userEmail"

                                )
                            } else {
                                Toast.makeText(
                                    context,
                                    "Se ha producido un error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                    }

                }
        }

        private fun addRouteToUser(userEmail: String, date: String, db: FirebaseFirestore) {
            Log.d("RequestRouteActivity", "userEmail: $userEmail")
            Log.d("RequestRouteActivity", "date: $date")
            db.collection("users").document(userEmail).update(
                "addedRoutes", FieldValue.arrayUnion(date)
            )

        }

    }

}