package com.amm.valleytraildam.data

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.amm.valleytraildam.ui.view.adminview.AdminHomeActivity
import com.google.firebase.firestore.FirebaseFirestore

class AdminCreateRoute {
    companion object {
        fun AdminCreateRoute(
            date: String,
            maxParticipants: Int,
            routeName: String,
            time: String,
            context: Context
        ) {

            val routeData = hashMapOf(
                "date" to date,
                "maxParticipants" to maxParticipants,
                "participants" to 0,
                "routeName" to routeName,
                "time" to time,
                "users" to ArrayList<String>(),
                "isBloqued" to false,
                "isActive" to true
            )

            val db = FirebaseFirestore.getInstance()


            db.collection("active_routes")
                .document(date)
                .set(routeData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Ruta creada", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, AdminHomeActivity::class.java)
                    startActivity(context, intent, null)

                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error al guardar", Toast.LENGTH_SHORT).show()
                }
        }
    }
}


