package com.amm.valleytraildam.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.amm.valleytraildam.ui.view.UserHomeActivity
import com.amm.valleytraildam.ui.view.AdminHomeActivity
import com.google.firebase.auth.FirebaseAuth

class Login {
    companion object {


        fun loginUser(email: String, password: String, context: Context) {

            Log.i("Login", "$email, $password")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {

                        AdminCheck.isAdmin(email) { isAdmin ->
                            if (isAdmin) {
                                val intent = Intent(context, AdminHomeActivity::class.java)
                                context.startActivity(intent)
                            } else {
                                val intent = Intent(context, UserHomeActivity::class.java)
                                context.startActivity(intent)
                            }
                        }


                    } else {
                        Toast.makeText(
                            context,
                            "Datos incorrectos",
                            Toast.LENGTH_LONG
                        ).show()
                    }


                }.addOnFailureListener {
                    Toast.makeText(
                        context,
                        "Error en la conexion ",
                        Toast.LENGTH_LONG
                    ).show()
                }

        }



    }
}