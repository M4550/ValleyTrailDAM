package com.amm.valleytraildam.utils

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class AdminCheck {

    companion object{
        fun isAdmin(email: String, onComplete: (Boolean) -> Unit) {
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(email).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val documentSnapshot = task.result

                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            // El documento existe, ahora intenta obtener el valor de isAdmin
                            val isAdmin = documentSnapshot.getBoolean("isAdmin")

                            // Verificar si isAdmin es verdadero o falso
                            onComplete(isAdmin ?: false)
                        } else {
                            Log.e("Error", "El documento no existe")
                            onComplete(false)
                        }
                    } else {
                        Log.e("Error", "Error al obtener el documento", task.exception)
                        onComplete(false)
                    }
                }

        }


    }
}