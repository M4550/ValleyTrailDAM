package com.amm.valleytraildam.ui.viewmodel

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
        fun registerUser(user: User, context: Context ) {

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
                                        "bornDate" to user.bornDate,
                                        "isAdmin" to false
                                    )
                                )
                            Toast.makeText(context, "Usuario creado", Toast.LENGTH_LONG).show()

                            val intent = Intent(context, LoginActivity::class.java)
                            startActivity(context, intent, null)

                        } else {

                            Log.e("Error", it.exception.toString())
                            val exception = it.exception.toString()
                            Toast.makeText(
                                context,
                                "Error: $exception",
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