package com.amm.valleytraildam.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import androidx.lifecycle.viewModelScope
import com.amm.valleytraildam.model.model.User

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
