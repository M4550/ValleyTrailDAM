package com.amm.valleytraildam.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.amm.valleytraildam.ui.view.userview.UserHomeActivity
import com.amm.valleytraildam.ui.view.adminview.AdminHomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class Login {
    companion object {

        fun loginUser(email: String, password: String, context: Context) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Inicio de sesión exitoso con correo y contraseña

                        // Verificar si el usuario es administrador
                        AdminCheck.isAdmin(email) { isAdmin ->
                            if (isAdmin) {
                                // Usuario es administrador
                                val intent = Intent(context, AdminHomeActivity::class.java)
                                intent.putExtra("email", email)
                                intent.putExtra("provider", "basic")
                                context.startActivity(intent)
                            } else {
                                // Usuario no es administrador
                                val intent = Intent(context, UserHomeActivity::class.java)
                                intent.putExtra("email", email)
                                intent.putExtra("provider", "basic")
                                context.startActivity(intent)
                            }
                        }
                    } else {
                        // Inicio de sesión con correo y contraseña fallido
                        // Mostrar mensaje de error
                        Toast.makeText(context, "Datos incorrectos", Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener { e ->
                    // Error al intentar iniciar sesión con correo y contraseña
                    // Mostrar mensaje de error
                    Toast.makeText(context, "Error en la conexión: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }

        fun loginUserWithGoogle(idToken: String?, context: Context) {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Inicio de sesión exitoso con Google

                        // Obtener el correo electrónico del usuario
                        val email = FirebaseAuth.getInstance().currentUser?.email

                        // Verificar si el usuario es administrador
                        AdminCheck.isAdmin(email ?: "") { isAdmin ->
                            if (isAdmin) {
                                // Usuario es administrador
                                val intent = Intent(context, AdminHomeActivity::class.java)
                                intent.putExtra("email", email)
                                intent.putExtra("provider", "google")
                                context.startActivity(intent)
                            } else {
                                // Usuario no es administrador
                                val intent = Intent(context, UserHomeActivity::class.java)
                                intent.putExtra("email", email)
                                intent.putExtra("provider", "google")
                                context.startActivity(intent)
                            }
                        }
                    } else {
                        // Inicio de sesión con Google fallido
                        // Mostrar mensaje de error
                        Toast.makeText(context, "Error al iniciar sesión con Google", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}