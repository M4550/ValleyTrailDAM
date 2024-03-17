package com.amm.valleytraildam.data

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.amm.valleytraildam.model.User
import com.amm.valleytraildam.ui.view.mainview.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DataBaseRegister {
    companion object {
        fun registerUser(user: User, context: Context) {

            val db = FirebaseFirestore.getInstance()


            user.email?.let { email ->
                user.password?.let { password ->
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        email, password
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            db.collection("users").document(user.email!!).set(
                                    hashMapOf(
                                        "nif" to user.nif,
                                        "email" to user.email,
                                        "address" to user.address,
                                        "name" to user.name,
                                        "phone" to user.phone,
                                        "password" to user.password,
                                        "surname" to user.surname,
                                        "bornDate" to user.bornDate,
                                        "isAdmin" to false
                                    )
                                )
                            Toast.makeText(context, "Usuario creado", Toast.LENGTH_LONG).show()

                            val intent = Intent(context, LoginActivity::class.java)
                            startActivity(context, intent, null)

                        } else {
                            Toast.makeText(context, "Error al crear usuario", Toast.LENGTH_LONG)
                                .show()

                        }

                    }
                }
            }

        }

        fun registerGoogleUser(email: String, name: String, context: Context) {
            val db = FirebaseFirestore.getInstance()

            // Verificar si el usuario ya está registrado
            db.collection("users").document(email).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        // El usuario ya está registrado, no es necesario volver a registrar
                        Log.d(TAG, "El usuario ya está registrado en la base de datos")
                    } else {
                        // El usuario no está registrado, registrarlos ahora
                        val userMap = mapOf(
                            "nif" to "",
                            "email" to email,
                            "address" to "",
                            "name" to name,
                            "phone" to "",
                            "password" to "",
                            "surname" to "",
                            "bornDate" to "",
                            "isAdmin" to false
                        )

                        // Crear un nuevo documento de usuario en la colección "users"
                        db.collection("users").document(email).set(userMap)
                            .addOnSuccessListener {
                                Log.d(TAG, "Usuario registrado correctamente en Firestore")
                            }
                            .addOnFailureListener { e ->
                                Log.e(TAG, "Error al registrar usuario en Firestore", e)
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error al verificar la existencia del usuario en Firestore", e)
                }
        }




    }


}
