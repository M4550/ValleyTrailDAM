package com.amm.valleytraildam.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.amm.valleytraildam.model.Route
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.amm.valleytraildam.model.User

class UserDataViewModel : ViewModel() {
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





}
