package com.amm.valleytraildam.ui.viewmodel

import android.annotation.SuppressLint
import android.graphics.Color
import android.icu.util.Calendar
import android.util.Log
import android.view.View
import com.amm.valleytraildam.model.model.Route
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.amm.valleytraildam.model.model.User
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception


class CheckRouteAvailability {

    companion object{



        private lateinit var normalizedEmail: String
        private lateinit var user: User




        fun checkAvailability(year :Int, month: Int, dayOfMonth: Int) : Boolean {
            val dayOfWeek = getDayOfWeek(year, month, dayOfMonth)
            val date = "$year-$month-$dayOfMonth"


            CoroutineScope(Dispatchers.IO).launch {

                val db = FirebaseFirestore.getInstance()
                val userDocRef = db.collection("users")
                    .document(FirebaseAuth.getInstance().currentUser!!.email.toString())
                val documentSnapshot = userDocRef.get().await()
                user = documentSnapshot.toObject<User>()!!
                normalizedEmail = user.email.toString()

            }

            val db = FirebaseFirestore.getInstance()

            db.collection("active_routes").document(date).get()
                .addOnSuccessListener { documentSnapshot ->

                    val activeRoute = documentSnapshot.toObject(Route::class.java)
                    Log.e("Info DB", "ActiveRoute data: $activeRoute")


                    if (activeRoute?.date != null) {


                        !activeRoute.users!!.contains(normalizedEmail)


                    }


                }.addOnFailureListener {
                    Exception("Error en la conexión", it)
                }

            return false
        }


        private fun getDayOfWeek(year: Int, month: Int, dayOfMonth: Int): Int {
            val calendar = Calendar.getInstance()
            calendar.set(dayOfMonth, month, year)
            return calendar.get(Calendar.DAY_OF_WEEK)
        }
    }


}