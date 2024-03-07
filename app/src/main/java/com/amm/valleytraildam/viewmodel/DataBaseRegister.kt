package com.amm.valleytraildam.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.amm.valleytraildam.model.User
import com.amm.valleytraildam.view.UserHomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Delay

class DataBaseRegister {
    companion object {
        fun registerUser(user: User, context: Context ) {

            Log.i("prueba", user.toString())

            val db = FirebaseFirestore.getInstance()
            user.email?.let { email->
                user.password?.let { password ->
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        email,
                        password
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            db.collection("users").document(user.email!!)
                                .set(
                                    hashMapOf(

                                        "nif" to user.nif,
                                        "email" to user.email,
                                        "address" to user.address,
                                        "name" to user.name,
                                        "phone" to user.phone,
                                        "password" to user.password,
                                        "surname" to user.surname,
                                        "date" to user.bornDate,
                                        "isAdmin" to false

                                    )
                                )
                            Toast.makeText(context, "Usuario creado", Toast.LENGTH_LONG).show()

                            val intent = Intent(context, UserHomeActivity::class.java)
                            intent.putExtra("email", user.email)
                            intent.putExtra("name", user.name)
                            startActivity(context, intent, null)

                        } else {
                            Toast.makeText(
                                context,
                                "El usuario ya existe o no tienes conexion a internet",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }

                    }
                }
            }
        }
    }
}