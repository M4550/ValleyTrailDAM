package com.amm.valleytraildam.ui.viewmodel.userviewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.amm.valleytraildam.model.User
import com.google.firebase.firestore.SetOptions

class UserDataViewModel : ViewModel() {


    companion object {
        suspend fun getUserData(): User? {
            return withContext(Dispatchers.IO) {
                val db = FirebaseFirestore.getInstance()
                val userId = FirebaseAuth.getInstance().currentUser?.email

                if (userId != null) {
                    val userDocument = db.collection("users").document(userId).get().await()
                    userDocument.toObject(User::class.java)
                } else {
                    null
                }
            }
        }

        suspend fun saveUserData(
            newAddress: String,
            newNif: String,
            newPhone: String,
            newName: String,
            newSurname: String
        ) {
            withContext(Dispatchers.IO) {
                val db = FirebaseFirestore.getInstance()
                val userEmail = FirebaseAuth.getInstance().currentUser?.email.toString()
                Log.i("USEREMAIL", userEmail)

                val userRef = db.collection("users").document(userEmail)
                val userData = hashMapOf(
                    "address" to newAddress,
                    "nif" to newNif,
                    "phone" to newPhone,
                    "name" to newName,
                    "surname" to newSurname
                )

                // Actualiza los datos del usuario en Firestore
                userRef.set(userData, SetOptions.merge()).await()

            }
        }
    }



}
